package br.edu.iffar.fw.classBag.interfaces;

import br.com.feliva.authClass.dao.PermissaoDAO;
import br.com.feliva.authClass.models.Permissao;
import br.edu.iffar.fw.classBag.db.dao.CursosDAO;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.util.MessagesUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.RollbackException;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Validators;
import org.omnifaces.util.selectitems.SelectItemsBuilder;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.file.UploadedFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Named
@ViewScoped
@Getter
@Setter
public class UserCSVFragmentBean implements Serializable {

    ConfigsCSVImpl configs;
    GrupoProcessamento grupoProcessamento = new GrupoProcessamento();

    List<Curso> listCursos;

    List<Permissao> listPermissao = new ArrayList<>();
    DualListModel<Permissao> dualListPermissoa = new DualListModel<>();

    List<GrupoProcessamento> listFileImports = new ArrayList<>();

    boolean rendNovoGrupoProcessamento = false;

    @Inject
    CursosDAO cursosDAO;
    @Inject
    PermissaoDAO permissaoDAO;
    @Inject
    MessagesUtil messagesUtil;

    @PostConstruct
    public void init(){
        this.configs = (ConfigsCSVImpl) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("configs");

        if(this.configs == null){
            this.configs = new ConfigsCSVImpl();
        }

        this.listCursos = this.cursosDAO.listAllCursos();
        this.listPermissao = this.permissaoDAO.listAll();
    }

    public void novoGrupoArquivos(){
        this.grupoProcessamento = new GrupoProcessamento();

        this.dualListPermissoa.setSource(this.listPermissao);
        this.dualListPermissoa.setTarget(this.grupoProcessamento.getListPermissoes());
    }

    public void criarNovoGrupoProcessamento(){
        this.novoGrupoArquivos();
        this.rendNovoGrupoProcessamento = true;
    }

    public void voltarImportarUsuario(){
        if(this.grupoProcessamento != null){
            this.enviar();
        }
        PrimeFaces.current().dialog().closeDynamic(this.configs);
    }


    public void enviar(){
        Set<ConstraintViolation<Object>> violations = Validators.getBeanValidator().validate(this.grupoProcessamento);
        if(!violations.isEmpty()){
            this.messagesUtil.addError(violations);
            return;
        }

        if(!this.grupoProcessamento.usarColunaPermissao) {
            this.grupoProcessamento.setListPermissoes(this.dualListPermissoa.getTarget());
        }
        this.configs.getListGrupoProcessamentos().add(this.grupoProcessamento);
        this.messagesUtil.addSuccess("Grupo para processamento enviado com sucesso!");
        this.rendNovoGrupoProcessamento = false;
        this.grupoProcessamento = null;
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.messagesUtil.addSuccess("Arquivo enviado com sucesso!");
    }

    public void handleFilesUpload(FilesUploadEvent event) {
        int contem = 0;
        int add = 0;
        for (UploadedFile up : event.getFiles().getFiles()){
            FileInMemory f = new FileInMemory(up.getFileName(), up.getSize(),up.getContent());
            if(this.grupoProcessamento.getListFile().contains(f)) {
                contem++;
            }else{
                this.grupoProcessamento.getListFile().add(f);
                add++;
            }
        };

        String addPlural = (add >1?"s":"");
        String contemPlural = (contem >1?"s":"");

        if(add > 0) {
            this.messagesUtil.addSuccess(add + " arquivo" + addPlural + " enviado" + addPlural + " com sucesso!");
        }
        if(contem >0) {
            this.messagesUtil.addWarn(contem + " arquivo" + contemPlural + " já enviado" + contemPlural + " anteriormente!");
        }
    }

    public List<SelectItem> getListDelimitatadorColuna() {
        return new SelectItemsBuilder().add(",", ",").add(";", ";").add("|", "|").buildList();
    }

    public List<SelectItem> getListCodificacaoArquivo() {
        return new SelectItemsBuilder().add("WINDOWS-1252", "WINDOWS-1252").add("ISO-8859-1", "ISO-8859-1").add("UTF-8", "UTF-8").buildList();
    }

    public List<SelectItem> getListDelimitadorTexto() {
        return new SelectItemsBuilder().add(null, "Não usar").add("\"", "\"").add("\'", "\'").buildList();
    }

}
