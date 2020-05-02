package com.lokesh.springbatchexample1.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.lokesh.springbatchexample1.model.User;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {


    /**
     * this is a main function of our application
     * @param jobBuilderFactory its implementation is provided in jobBuilderFactory.get("ETL-Load")
     * @param stepBuilderFactory its implementation is provided in stepBuilderFactory.get("ETL-file-load")
     * @param itemReader its implementation is provided in itemReader() method 
     * @param itemProcessor its implementation is provided in Processor.java class
     * @param itemWriter its implementation is provided in DBWriter.java class
     * 
     */
    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   StepBuilderFactory stepBuilderFactory,
                   ItemReader<User> itemReader,
                   ItemProcessor<User, User> itemProcessor,
                   ItemWriter<User> itemWriter
    ) {

//    	below code creates step, steps has reader,processor and writter
//    	instead of ETL-file-load we will be able to give any name 
        Step step = stepBuilderFactory.get("ETL-file-load")
                .<User, User>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();

//        below code is for to build a job

        return jobBuilderFactory.get("ETL-Load")
//        		this will increment job run id each time when job runs,if we want to crate our own incrementor
                .incrementer(new RunIdIncrementer())
//                if we have multiple steps then instead of using .start(step) we can use .flow(step).next(step).next(step2)
                .start(step)
//               .build()  indicated the end of the step
                .build();
    }

//    this is reader bean,we are reading csv file so we are using inbulit class called FlatFileItemReader
    @Bean
    public FlatFileItemReader<User> itemReader() {

        FlatFileItemReader<User> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("src/main/resources/users.csv"));
        flatFileItemReader.setName("CSV-Reader");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(lineMapper());
        return flatFileItemReader;
    }

    
//    this function is to map the row which is read from csv file to the class means User class
    @Bean
    public LineMapper<User> lineMapper() {

        DefaultLineMapper<User> defaultLineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();

        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames(new String[]{"id", "name", "dept", "salary"});

        BeanWrapperFieldSetMapper<User> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(User.class);

        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(fieldSetMapper);

        return defaultLineMapper;
    }

}
