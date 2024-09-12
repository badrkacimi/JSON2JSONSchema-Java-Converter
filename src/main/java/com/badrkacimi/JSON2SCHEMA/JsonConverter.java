package com.badrkacimi.JSON2SCHEMA;

import com.sun.codemodel.JCodeModel;
import org.apache.commons.io.FileUtils;
import org.jsonschema2pojo.*;
import org.jsonschema2pojo.rules.RuleFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class JsonConverter {

    public static void converting_Json2Pojo(String packageName, File outputPojoDirectory, List<File> files, int i)
            throws MalformedURLException {
        for (File file : files) {
            URL inputjson = file.toURI().toURL();
            String className = file.getName();

            try {
                new JsonConverter().convert2JSON(inputjson, outputPojoDirectory, packageName, className.replace(".json", ""));
                i++;


            } catch (Exception e) {
                System.out.println("Encountered issue while converting to pojo: " + e.getMessage());
                e.printStackTrace();
            }

        }
        System.out.println("Number of POJOs converted & created: " + " " + (i - 1));
    }

    public static List<File> gettingJson_files() throws IOException {
        File dir = new File("./src/main/resources/json/json-files");// input
        String[] extensions = new String[]{"json"};
        System.out.println("getting JSON files... " + dir.getCanonicalPath() + " including those in the subdirectories.");
        return (List<File>) FileUtils.listFiles(dir, extensions, true);
    }

    public void convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className) throws IOException {
        JCodeModel codeModel = new JCodeModel();
        GenerationConfig config = new DefaultGenerationConfig() {
            @Override
            public boolean isGenerateBuilders() { // set config option by overriding method
                return true;
            }

            public SourceType getSourceType() {
                return SourceType.JSON;
            }
        };
        SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());
        mapper.generate(codeModel, className, packageName, inputJson);
        codeModel.build(outputPojoDirectory);
    }
}
