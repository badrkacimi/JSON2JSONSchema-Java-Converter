package com.badrkacimi.schemagenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Author Badr Kacimi
 */
public class JacksonJsonSchemaGenerator {
    public static void main(String[] args)
            throws ClassNotFoundException {

        try {
            Class[] classes = getClasses("com.hiveprod.pojogen");//input
            for (Class clazz : classes) {

                StringWriter jsonSchemafile = generateSchemaMapping(clazz);
                createFileWithOption(clazz, jsonSchemafile);
            }
        } catch (IOException e) {
            System.out.println("Problem encountered during conversion to JSON Schema: " + e.getMessage());
            e.printStackTrace();
        }

    }


    private static StringWriter generateSchemaMapping(Class clazz)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator generator = new JsonSchemaGenerator(mapper);

        JsonSchema jsonSchema = generator.generateSchema(clazz);
        StringWriter jsonSchemafile = new StringWriter();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        //mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
        mapper.writeValue(jsonSchemafile, jsonSchema);
        return jsonSchemafile;
    }

    private static void createFileWithOption(Class clazz, StringWriter jsonSchemafile) throws IOException {
        // output
        File filegeneree = new File("./src/main/resources/generated/" + clazz.getName().replace("java", "").replace("com.badrkacimi.pojogen.", "") + ".jsonschema");
        filegeneree.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(filegeneree.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(jsonSchemafile.toString());
        System.out.println(filegeneree.getCanonicalPath());
        bw.close();
    }

    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
