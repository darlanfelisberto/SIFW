//package br.edu.iffar.fw.ru.bean.fragment;
//
//import br.edu.iffar.fw.classBag.db.model.Curso;
//import br.edu.iffar.fw.ru.bean.ImportarUsuariosBean;
//import org.apache.commons.csv.CSVRecord;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class criaImportFile {
//
//
//    private List<ImportarUsuariosBean.FileImport> listFileImport = new ArrayList<>();
//    private ImportarUsuariosBean.FileImport fileImport = new ImportarUsuariosBean.FileImport();
//
//    public record FileImport
//    public class FileImports{
//        protected boolean inativarMatriculaAusente = false;
//        protected int firstRecord;
//        protected Integer colunaRole;
//        private Integer columaCurso;
//        protected Curso curso;
//        private List<CSVRecord> listRescord;
//        private String nomeArquivo;
//
//        public boolean isInativarMatriculaAusente() {
//            return inativarMatriculaAusente;
//        }
//
//        public void setInativarMatriculaAusente(boolean inativarMatriculaAusente) {
//            this.inativarMatriculaAusente = inativarMatriculaAusente;
//        }
//
//        public int getFirstRecord() {
//            return firstRecord;
//        }
//
//        public void setFirstRecord(int firstRecord) {
//            this.firstRecord = firstRecord;
//        }
//
//        public Integer getColunaRole() {
//            return colunaRole;
//        }
//
//        public void setColunaRole(Integer colunaRole) {
//            this.colunaRole = colunaRole;
//        }
//
//        public Integer getColumaCurso() {
//            return columaCurso;
//        }
//
//        public void setColumaCurso(Integer columaCurso) {
//            this.columaCurso = columaCurso;
//        }
//
//        public Curso getCurso() {
//            return curso;
//        }
//
//        public void setCurso(Curso curso) {
//            this.curso = curso;
//        }
//
//        public List<CSVRecord> getListRescord() {
//            return listRescord;
//        }
//
//        public void setListRescord(List<CSVRecord> listRescord) {
//            this.listRescord = listRescord;
//        }
//
//        public String getNomeArquivo() {
//            return nomeArquivo;
//        }
//
//        public void setNomeArquivo(String nomeArquivo) {
//            this.nomeArquivo = nomeArquivo;
//        }
//    }
//}
