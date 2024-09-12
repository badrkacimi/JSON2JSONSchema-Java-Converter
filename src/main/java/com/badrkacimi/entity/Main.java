package com.badrkacimi.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.badrkacimi.JSON2SCHEMA.JsonConverter.converting_Json2Pojo;
import static com.badrkacimi.JSON2SCHEMA.JsonConverter.gettingJson_files;

public class Main {

    public static void main(String[] args) throws IOException {

        String packageName = "com.badrkacimi.pojogen"; // output package
        File outputPojoDirectory = new File("./src/main/java");

        List<File> files = gettingJson_files();

        int i = 0;

        converting_Json2Pojo(packageName, outputPojoDirectory, files, i);

    }


}
