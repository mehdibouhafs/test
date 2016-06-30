package configurations;

/**
 * Created by MBS on 30/06/2016.
 */
public class ConfigByClass {
   /* public class CsvFileToDatabaseJobConfig {

        @Bean
        ItemReader<StudentDTO> csvFileItemReader() {
            FlatFileItemReader<StudentDTO> csvFileReader = new FlatFileItemReader<>();
            csvFileReader.setResource(new ClassPathResource("data/students.csv"));
            csvFileReader.setLinesToSkip(1);

            LineMapper<StudentDTO> studentLineMapper = createStudentLineMapper();
            csvFileReader.setLineMapper(studentLineMapper);

            return csvFileReader;
        }

        private LineMapper<StudentDTO> createStudentLineMapper() {
            DefaultLineMapper<StudentDTO> studentLineMapper = new DefaultLineMapper<>();

            LineTokenizer studentLineTokenizer = createStudentLineTokenizer();
            studentLineMapper.setLineTokenizer(studentLineTokenizer);

            FieldSetMapper<StudentDTO> studentInformationMapper = createStudentInformationMapper();
            studentLineMapper.setFieldSetMapper(studentInformationMapper);

            return studentLineMapper;
        }

        private LineTokenizer createStudentLineTokenizer() {
            DelimitedLineTokenizer studentLineTokenizer = new DelimitedLineTokenizer();
            studentLineTokenizer.setDelimiter(";");
            studentLineTokenizer.setNames(new String[]{"name", "emailAddress", "purchasedPackage"});
            return studentLineTokenizer;
        }

        private FieldSetMapper<StudentDTO> createStudentInformationMapper() {
            BeanWrapperFieldSetMapper<StudentDTO> studentInformationMapper = new BeanWrapperFieldSetMapper<>();
            studentInformationMapper.setTargetType(StudentDTO.class);
            return studentInformationMapper;
        }
    }*/
}
