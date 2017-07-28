import java.lang.reflect.Field;
import java.util.logging.Logger;

public class CustomAnnotationUtils {
	
	public static void getInfo(Class<?> clazz) {
		
		String name = "";
		String gender = "";
		String profile = "";
		Field fields[] = clazz.getDeclaredFields();
		for(Field field:fields) {
			if(field.isAnnotationPresent(Name.class)) {
				Name arg0 = field.getAnnotation(Name.class);
				name = name + arg0.value();
				Logger.getLogger("gjc").info("name = " + name);
			}
			if(field.isAnnotationPresent(Gender.class)) {
				Gender arg1 = field.getAnnotation(Gender.class);
				gender = gender + arg1.gender();
				Logger.getLogger("gjc").info("gander = " + gender);
			}
			if(field.isAnnotationPresent(Profile.class)) {
				Profile arg2 = field.getAnnotation(Profile.class);
				profile = profile + arg2.nativePlace();
				Logger.getLogger("gjc").info("profile = " + profile);
			}
		}
	}
	
	public static void main(String[] argo) {
		getInfo(Person.class);
	}
}
