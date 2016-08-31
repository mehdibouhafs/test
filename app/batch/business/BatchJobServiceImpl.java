package batch.business;

import batch.dao.BatchJobDao;
import batch.util.Generator;
import batch.listeners.JobCompletionNotificationListener;
import batch.model.*;
import batch.model.Reader;
import batch.model.batch.BatchStepExecution;
import batch.util.ReadXMLFile2;
import javassist.CannotCompileException;
import javassist.NotFoundException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import running.Global;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.Blob;
import java.util.*;

/**
 * Created by MBS on 08/08/2016.
 */
public class BatchJobServiceImpl implements BatchJobService {

    private Reader reader;
    private ApplicationContext context;
    private BatchJobDao batchJobDao;


    public BatchJobServiceImpl() {
        context = Global.getApplicationContext();
    }

    public BatchJobServiceImpl(BatchJobDao batchJobDao) {
        this.batchJobDao = batchJobDao;
    }

    public BatchJobServiceImpl(Reader reader, BatchJobDao batchJobDao) {
        this.reader = reader;
        context = Global.getApplicationContext();
    }

    public Map<String, Class<?>> typeAttributes(List<Attribute> attributes,Reader reader) {
        Map<String, Class<?>> properties = new LinkedHashMap<>();
        String[] cols = reader.columns.split(",");
        if(Classe.find.byId(reader.classeName)!= null){
            Attribute.dropInvolving(reader.classeName);
        }else{
            Classe classe = new Classe();
            classe.className = reader.classeName;
            classe.user_email = reader.emailUser;
            classe.save();
        }
        for (int i = 0; i < attributes.size(); i++) {
            Class o;
            switch (attributes.get(i).getType()) {
                case "NUMBER":
                    o = Integer.class;
                    break;
                case "CHAR":
                    o = Character.class;
                    break;
                case "SMALLINT":
                    o = Integer.class;
                    break;
                case "BLOB":
                    o = Blob.class;
                    break;
                case "DOUBLE":
                    o = Double.class;
                    break;
                case "DECIMAL":
                    o = Double.class;
                    break;
                case "REAL":
                    o = Float.class;
                    break;
                case "BIT":
                    o = Byte.class;
                    break;
                case "BOOLEAN":
                    o = Boolean.class;
                    break;
                case "MEDIUMINT":
                    o = Integer.class;
                    break;
                case "BIGINT":
                    o = Integer.class;
                    break;
                case "VARCHAR2":
                    o = String.class;
                    break;
                case "VARCHAR":
                    o = String.class;
                    break;
                case "DATETIME":
                    o = Date.class;
                    break;
                case "DATE":
                    o = Date.class;
                    break;
                default:
                    o = Object.class;
                    break;

            }
            attributes.get(i).save();
            properties.put(attributes.get(i).nameo, o);
        }
        for (String s: cols
             ) {
            Class<?> val = properties.get(s);
            if (val == null) {
                properties.put(s, Object.class);
                Attribute attribute = new Attribute();
                attribute.nameo = s;
                attribute.type = "object";
                attribute.sizeo="";
                attribute.commentaires="";
                attribute.classe = reader.classeName;
                attribute.save();
            }
        }
        return properties;
    }


    public Map<String, Class<?>> typeAttributes1(List<Attribute> attributes,Reader reader) {
        Map<String, Class<?>> properties = new LinkedHashMap<>();
        String[] cols = reader.columns.split(",");

        for (int i = 0; i < attributes.size(); i++) {
            Class o;

            System.out.println("Switch type " + attributes.get(i).getType());

            switch (attributes.get(i).getType()) {
                case "NUMBER":
                    o = Integer.class;
                    break;
                case "CHAR":
                    o = Character.class;
                    break;
                case "SMALLINT":
                    o = Integer.class;
                    break;
                case "BLOB":
                    o = Blob.class;
                    break;
                case "DOUBLE":
                    o = Double.class;
                    break;
                case "DECIMAL":
                    o = Double.class;
                    break;
                case "REAL":
                    o = Float.class;
                    break;
                case "BIT":
                    o = Byte.class;
                    break;
                case "BOOLEAN":
                    o = Boolean.class;
                    break;
                case "MEDIUMINT":
                    o = Integer.class;
                    break;
                case "BIGINT":
                    o = Integer.class;
                    break;
                case "VARCHAR2":
                    o = String.class;
                    break;
                case "VARCHAR":
                    o = String.class;
                    break;
                case "DATETIME":
                    o = Date.class;
                    break;
                case "DATE":
                    o = Date.class;
                    break;
                default:
                    o = Object.class;
                    break;

            }
            properties.put(attributes.get(i).nameo, o);
        }
        for (String s: cols
                ) {
            Class<?> val = properties.get(s);
            if (val == null) {
                properties.put(s, Object.class);
            }
        }
        return properties;
    }



    public Map<String, String> columnsWithTypeAndSize(List<Attribute> attributes){
       /* Map<String, String> columnTable = new LinkedHashMap<>();
        StringBuffer typeSizes;
        for (int i = 0; i < attributes.size(); i++) {
            typeSizes = new StringBuffer(attributes.get(i).getType()+"-"+attributes.get(i).getSizeo());
            if(attributes.get(i).isNonNull()== true){
                typeSizes.append("- NOT NULL");
            }else {
                typeSizes.append("-");
            }
            if(attributes.get(i).isPko()== true){
                typeSizes.append("PrimaryKey");
            }else{
                typeSizes.append("-");
            }
            if(attributes.get(i).getDatac() != null) {
                typeSizes.append("- DEFAULT '" + attributes.get(i).getDatac() + "'");
            }else{
                typeSizes.append("- DEFAULT ''");
            }
            if(attributes.get(i).getCommentaires() !=null) {
                typeSizes.append("- " + attributes.get(i).getCommentaires());
            }else{
                typeSizes.append("- ");
            }
            columnTable.put(attributes.get(i).getNameo(), typeSizes.toString());
        }
            */
        return  null;
    }

    @Override
    public Map<String, String> dataTable(String table) {
        return batchJobDao.dataTable(table);
    }


    public String getExtension(String fileName) {
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return extension;
    }


    @Override
    public Resume doJob(Reader reader) {
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        String databases = batchJobDao.buildCdata(reader,Attribute.findInvolving(reader.classeName));
        System.out.println("databases "  +databases);
        String[] cData = databases.split(";");
        if(reader.executed == true){
            System.out.println("reader true");
            batchJobDao.executer("truncate table " + reader.classeName);
        }
        else{
        batchJobDao.executer(cData[1]);
            /*List<String> comments = batchJobDao.getCommentaires(reader.classeName,Attribute.findInvolving(reader.classeName));
            if(comments.size()>1){
            for (String com :comments){
                if(!com.contains("nul")) {
                    batchJobDao.executer(com);
                }
            }
            reader.executed = true;
            }*/
        }
        System.out.println("his");
        System.out.println("reader " + reader );
        File destination = new File(reader.filePath);
        JobParameters param = new JobParametersBuilder()
                .addString("input.file.name", destination.getPath())
                .addString("separator",reader.separator)
                .addString("columns",reader.columns)
                .addString("email",reader.emailUser)
                .addString("fragmentRoot",reader.tableName)
                .addString("nbLineToSkip",reader.nbLineToSkip+"")
                .addString("cData",cData[0])
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
        listener.setUser(User.find.byId(reader.executed_by));
        System.out.println("hisssss");
        try {
            if(getExtension(reader.filePath).equals("xml")){
                getElementAndAttributesFileXml(reader);
            }
            Generator c = context.getBean("generator", Generator.class);
            List<Attribute> attributes = Reader.getAttributeByReader(reader.id);
            System.out.println("Attributes jjob " + attributes);
            c.setProperties(typeAttributes1(attributes,reader));
            System.out.println("Properties " + c.getProperties());
            Reader reader1 = context.getBean("reader",Reader.class);
            Class<?> classNew = null;
                if(getExtension(reader.filePath).equals("csv")){
                    classNew = c.generator(reader.classeName,"csv");
                    System.out.println("Classe NEw " + classNew);
                } else{
                    classNew = c.generator(reader.tableName,"xml");
                    String[] a = classNew.toString().split(" ");
                    reader1.packageClasse = a[1];
                }
                reader1.classeGenerate=classNew;
            Job job;
            if (getExtension(reader.filePath).equals("csv")) {
               job  = (Job) context.getBean("csvJob");
            }else {
                job = (Job) context.getBean("xmlJob");
            }
            JobExecution jobExecution = jobLauncher.run(job, param);
                if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                    reader.resultat = true;
                    reader.jobId = jobExecution.getId();
                    reader.nbLinesSuccess = BatchStepExecution.findByJobExecID(jobExecution.getId()).getWrite_count();
                    reader.nbLinesFailed = (BatchStepExecution.findByJobExecID(jobExecution.getId()).getCommit_count()-1) - reader.nbLinesSuccess;
                    List<InputError> inputErros = InputError.find.where().eq("job_execution_id",jobExecution.getId()).findList();
                    Resume resume = new Resume();
                    System.out.println("job ID " + jobExecution.getId());
                    System.out.println("Batch "+ BatchStepExecution.find.byId(jobExecution.getId()));
                    resume.setBatchStepExecution(BatchStepExecution.findByJobExecID(jobExecution.getId()));
                    resume.setInputError(inputErros);
                    Generator c1 = context.getBean("generator", Generator.class);
                    c1.setClassGenerate(null);
                    reader.update();
                    return resume;
                }else{
                    reader.resultat = false;
                    reader.update();
                    //batchJobDao.dropTable(reader.classeName);
                    /// /Classe.dropClasseAndAttribute(classe);
                }
        } catch (FlatFileParseException e) {
            System.out.println("CATCH IT ");
            e.printStackTrace();
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> firstLineCsvFile(File f, String delimiter)  {
        System.out.println("firstline Delimiter " + delimiter);
        FileReader fr = null;
        try {
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String quotes = "\"";
            if (delimiter != null) {
                String line0 = br.readLine().replaceAll(quotes, "");
                String[] line = line0.split(delimiter);
                br.close();
                System.out.println("firstline line" + Arrays.asList(line));
                return Arrays.asList(line);
            } else {
                String[] s = br.readLine().split(",");
                br.close();
                return Arrays.asList(s);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
        //System.out.println(line);
        //rows.setRow(row);
    }

    public String[] getElementAndAttributesFileXml(Reader reader) {
        DocumentBuilder dBuilder = null;
        Reader reader1 = context.getBean("reader",Reader.class);
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(new File(reader.filePath));
            if (doc.hasChildNodes()) {
                ReadXMLFile2 readXMLFile2 = new ReadXMLFile2();
                readXMLFile2.printNote(doc.getChildNodes());
                String[] ss = new String[readXMLFile2.getS().size() - 2];
                int j = 0;
                reader.tableName= readXMLFile2.getS().get(1);
                for (int i = 2; i < readXMLFile2.getS().size(); i++) {
                    ss[j] = readXMLFile2.getS().get(i);
                    j++;
                }
                reader1.cols = (Arrays.asList(ss));
                String[] att = new String[readXMLFile2.getAtt().size()];
                for (int i = 0; i < readXMLFile2.getAtt().size(); i++) {
                    att[i] = readXMLFile2.getAtt().get(i);
                }
                reader.update();
                String[] both = (String[]) ArrayUtils.addAll(att, ss);
                return both;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





}
