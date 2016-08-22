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
    private String classeName;
    private ApplicationContext context;
    private BatchJobDao batchJobDao;


    public BatchJobServiceImpl() {
    }

    public BatchJobServiceImpl(BatchJobDao batchJobDao) {
        this.batchJobDao = batchJobDao;
    }

    public BatchJobServiceImpl(Reader reader, String classeName, BatchJobDao batchJobDao) {
        this.reader = reader;
        this.classeName = classeName;
        context = Global.getApplicationContext();



    }

    public Map<String, Class<?>> typeAttributes(List<Attribute> attributes) {
        System.out.println(" Attributess " + attributes);
        Map<String, Class<?>> properties = new LinkedHashMap<>();
        String[] cols = reader.columns.split(",");
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
                attribute.classe = this.classeName;
                attribute.save();
            }
        }
        Generator c = context.getBean("generator", Generator.class);
        c.setProperties(properties);
        Reader reader1 = context.getBean("reader",Reader.class);
        Class<?> classNew = null;
        try {
            if(getExtension(reader.filePath).equals("csv")){
                classNew = c.generator(classeName,"csv");
                System.out.println("Classe NEw " + classNew);
            } else{
                classNew = c.generator(reader.fragmentRootName,"xml");
                String[] a = classNew.toString().split(" ");

                reader1.packageClasse = a[1];
            }

            System.out.println(classNew +" Name "+ classNew.getName() + " sim "+ classNew.getSimpleName());
            if(Classe.isExistClasse(classeName)){
                Classe classe1 = Classe.find.byId(classeName);
                classe1.user_email = reader.emailUser;
                classe1.classeGenerated = classNew.getSimpleName();
                classe1.columns = reader.columns;
                //Attribute.dropInvolving(classeName);
                classe1.update();
            }else{
                Classe classe1 = new Classe();
                classe1.className = classeName;
                classe1.user_email = reader.emailUser;
                classe1.columns = reader.columns;
                classe1.classeGenerated = classNew.getName();
                classe1.save();
            }
            reader1.classeGenerate=classNew;
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return properties;
    }


    public Map<String, String> columnsWithTypeAndSize(List<Attribute> attributes){
        Map<String, String> columnTable = new LinkedHashMap<>();
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
            typeSizes.append("- DEFAULT '"+attributes.get(i).getDefautlVal()+"'");
            typeSizes.append("- "+attributes.get(i).commentaire);
            columnTable.put(attributes.get(i).getNameo(), typeSizes.toString());
        }

        return  columnTable;
    }

    @Override
    public boolean createTable(String name, Map<String, String> columnsTable,List<Attribute> attributes) {
        return batchJobDao.createTableOracle(name,columnsTable,attributes);
    }

    @Override
    public boolean dropTable(String name) {
        return batchJobDao.dropTable(name);
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
    public Resume doJob(Reader reader,String classe) {
        BatchJobDao batchJobDao = (BatchJobDao) context.getBean("batchJobDao");
        Classe classe1 = Classe.find.byId(classeName);
        classe1.cData = batchJobDao.getCdata();
        classe1.update();
        File destination = new File(reader.filePath);
        JobParameters param = new JobParametersBuilder()
                .addString("input.file.name", destination.getPath())
                .addString("separator",reader.separator)
                .addString("columns",reader.columns)
                .addString("email",reader.emailUser)
                .addString("nbLineToSkip",reader.nbLineToSkip+"")
                .addString("cData",batchJobDao.getCdata())
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        try {
            if (getExtension(reader.filePath).equals("csv")) {
                Job job = (Job) context.getBean("csvJob");
                JobExecution jobExecution = jobLauncher.run(job, param);
                if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                    List<InputError> inputErros = InputError.find.where().eq("job_execution_id",jobExecution.getId()).findList();
                    Resume resume = new Resume();
                    System.out.println("job ID " + jobExecution.getId());
                    System.out.println("Batch "+ BatchStepExecution.find.byId(jobExecution.getId()));
                    resume.setBatchStepExecution(BatchStepExecution.findByJobExecID(jobExecution.getId()));
                    resume.setInputError(inputErros);
                    Generator c = context.getBean("generator", Generator.class);
                    c.setClassGenerate(null);
                    return resume;
                }else{
                    batchJobDao.dropTable(classe);
                    Classe.dropClasseAndAttribute(classe);
                }
            }else{
                Job job = (Job) context.getBean("importXML");
                JobExecution jobExecution = jobLauncher.run(job, param);
                JobCompletionNotificationListener listener = context.getBean("listener", JobCompletionNotificationListener.class);
                if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                    List<InputError> inputErros = InputError.find.where().eq("job_execution_id",jobExecution.getId()).findList();
                    Resume resume = new Resume();
                    System.out.println("job ID " + jobExecution.getId());
                    System.out.println("Batch "+ BatchStepExecution.find.byId(jobExecution.getId()));
                    resume.setBatchStepExecution(BatchStepExecution.findByJobExecID(jobExecution.getId()));
                    resume.setInputError(inputErros);
                    Generator c = context.getBean("generator", Generator.class);
                    c.setClassGenerate(null);
                    return resume;
                }else{
                    batchJobDao.dropTable(classe);
                }
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
        }
        return null;
    }

    public String[] firstLineCsvFile(File f, String delimiter)  {
        FileReader fr = null;
        try {
            fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String quotes = "\"";
            if (delimiter != null) {
                String line0 = br.readLine().replaceAll(quotes, "");
                String[] line = line0.split(delimiter);
                br.close();
                return line;
            } else {
                String[] s = br.readLine().split(",");
                br.close();
                return s;
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

    public String[] getElementAndAttributesFileXml(File f) {
        DocumentBuilder dBuilder = null;
        Reader reader = Global.getApplicationContext().getBean("reader",Reader.class);
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            if (doc.hasChildNodes()) {
                ReadXMLFile2 readXMLFile2 = new ReadXMLFile2();
                readXMLFile2.printNote(doc.getChildNodes());
                String[] ss = new String[readXMLFile2.getS().size() - 2];
                int j = 0;
                reader.fragmentRootName= readXMLFile2.getS().get(1);
                for (int i = 2; i < readXMLFile2.getS().size(); i++) {
                    ss[j] = readXMLFile2.getS().get(i);
                    j++;
                }
                reader.cols = (Arrays.asList(ss));
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
