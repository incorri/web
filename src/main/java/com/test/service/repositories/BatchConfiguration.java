package com.test.service.repositories;

import com.test.service.models.Book;
import com.test.service.proc.BookItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Value("${upload.path}")
    private String uploadPath;

    private String loadedFile = "TemplateImportOU.csv";

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    ClassPathResource classPathResource = new ClassPathResource(loadedFile);

    @Bean
    public ItemReader<Book> reader(){
        FlatFileItemReader<Book> reader = new FlatFileItemReader<>();
        reader.setResource(classPathResource);
        reader.setLineMapper(new DefaultLineMapper<Book>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "titleName", "authorName", "pages", "publishingOffice"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Book>() {{
                setTargetType(Book.class);
            }});
        }});
        return reader;
    }

    @Bean
    public ItemProcessor<Book, Book> processor() {
        return new BookItemProcessor();
    }

    @Bean
    public ItemWriter<Book> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Book> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        writer.setSql("INSERT INTO fix_books (title_name, author_name, pages, publishing_office) " +
                "VALUES (:titleName, :authorName, :pages, :publishingOffice)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importBookJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("importBookJob")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Book> reader,
                      ItemWriter<Book> writer, ItemProcessor<Book, Book> processor) {
        return stepBuilderFactory.get("step1")
                .<Book, Book> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public void perform(String filename) throws Exception
    {
        classPathResource = new ClassPathResource("d:\\" + uploadPath + File.separator + filename);
        System.out.println(classPathResource.getPath());
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);
    }
}
