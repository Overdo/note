import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Gender {
	
	public enum GenderTyper {
		
		Male("男"),
		Female("女"),
		Other("中性");
		
		private String genderStr;
		
		private GenderTyper(String arg0) {
			this.genderStr = arg0;
		}

		@Override
		public String toString() {
			return super.toString();
		}
	}
	
	GenderTyper gender() default GenderTyper.Male;
	
}
