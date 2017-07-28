
public class Person {

	@Name("Achelice")
	private String name;
	@Gender(gender = Gender.GenderTyper.Male)
	private String gender;
	@Profile(id = 888,height=180,nativePlace = "CN")
	private String profile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
}
