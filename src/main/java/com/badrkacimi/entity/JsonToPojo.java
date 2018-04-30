		package com.badrkacimi.entity;  
		import java.io.File;
		import java.io.IOException;
		import java.net.MalformedURLException;
		import java.net.URL;
		import java.util.List;
		
		import org.apache.commons.io.FileUtils;
		import org.jsonschema2pojo.DefaultGenerationConfig;
		import org.jsonschema2pojo.GenerationConfig;
		import org.jsonschema2pojo.Jackson2Annotator;
		import org.jsonschema2pojo.SchemaGenerator;
		import org.jsonschema2pojo.SchemaMapper;
		import org.jsonschema2pojo.SchemaStore;
		import org.jsonschema2pojo.SourceType;
		import org.jsonschema2pojo.rules.RuleFactory;
		
		//import com.hiveprod.generator.service.OpenJpaPojoTransformationService;
		//import com.hiveprod.generator.service.OpenJpaPojoTransformationService;
		import com.sun.codemodel.JCodeModel;  
		public class JsonToPojo {  
		     /**  
		  * @param args  
		 * @throws IOException 
		  */  
		 public static void main(String[] args) throws IOException {  
			 
		     String packageName="com.hiveprod.pojogen"; // output package    
		 File outputPojoDirectory=new File("./src/main/java");  
		
		     List<File> files = gettingJson_files();
		     
			int i = 0;
			
			converting_Json2Pojo(packageName, outputPojoDirectory, files, i);  
		
		 }
			private static void converting_Json2Pojo(String packageName, File outputPojoDirectory, List<File> files, int i)
					throws MalformedURLException {
				for (File file : files) {
					URL inputjson = file.toURI().toURL();
					String className=file.getName();
		
					try {  
				        	 new JsonToPojo().convert2JSON(inputjson, outputPojoDirectory, packageName,className.replace(".json", ""));  
			        	    i++;
			                   
			           
			          } catch (Exception e) {  
			               System.out.println("Encountered issue while converting to pojo: "+e.getMessage());  
			               e.printStackTrace();  
			          }		
					
			}    
			  System.out.println("Nombre de POJOs converted & created: "+" "+ (i-1));
		}
		private static List<File> gettingJson_files() throws IOException {
			File dir = new File("./src/main/resources/json/json-files");// input
				String[] extensions = new String[] { "json" };
				System.out.println("Obtenir tous les json... " + dir.getCanonicalPath()+ " y compris ceux dans les sous-repertoires");
					List<File> files = (List<File>) FileUtils.listFiles(dir, extensions, true);
				return files;
			}  
		 public void convert2JSON(URL inputJson, File outputPojoDirectory, String packageName, String className) throws IOException
		    {  
		      JCodeModel codeModel = new JCodeModel();  
		      URL source = inputJson;  
		      GenerationConfig config = new DefaultGenerationConfig() {  
		          @Override  
		          public boolean isGenerateBuilders() { // set config option by overriding method  
			              return true;  
			          }  
		          
			          public SourceType getSourceType(){  
			        	  return SourceType.JSON;  
			          }  
		          };  
		          SchemaMapper mapper = new SchemaMapper(new RuleFactory(config, new Jackson2Annotator(config), new SchemaStore()), new SchemaGenerator());  
		          mapper.generate(codeModel, className, packageName, source);  
		          codeModel.build(outputPojoDirectory);  
		     }  
		}