package constraint;

final public class Constants {
	public enum EJButton {
		eSelect("file"), 
		eExe1("exe1"), 
		eExe2("exe2");

		private String name;

		EJButton(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
