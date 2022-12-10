package constraint;

public enum EJButton {
	eSelect("File", "File"), 
	eExe1("exe1", "exe1"), 
	eExe2("exe2", "exe2"),
	eExe3("exe3", "exe3(없는 파일)"),
	eExit("exit", "exit");

	private String name;
	private String label;
	EJButton(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public String getName() {
		return name;
	}
	public String getLabel() {
		return label;
	}
}
