"use strict";

class ListMeses {
	
	constructor(today) {
		this.ano = today.getFullYear();
		this.mes = today.getMonth();//0 a 11
		this.meses = ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'];
	}

	criaElemento() {
		console.log(this.ano);
		return `${this.mes}/${this.ano}`
	}
	
	cria12Link(){
		let listMes = document.getElementById('listMes');
		for(let c = 0;c<80;c++){
			
			let insert = `<div>${this.criaElemento()}</div>`
			listMes.innerHTML = listMes.innerHTML + insert; 
			
			this.mes--;
			
			if(this.mes == 0){
				this.mes = 12;
				this.ano--;
			}
			
		}		
	}
}


var l = new ListMeses(new Date());
l.cria12Link();
