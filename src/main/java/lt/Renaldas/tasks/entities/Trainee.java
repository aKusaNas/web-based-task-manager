package lt.Renaldas.tasks.entities;

public enum Trainee {

    NOT_ASSIGNED("NOT_ASSIGNED"),
    LINDA("LINDA"), 
    KAROLIS("KAROLIS"), 
    TOM("TOM");

    private final String displayValue;

    private Trainee(String displayValue) {
	this.displayValue = displayValue;
    }

    public String getDisplayValue() {
	return displayValue;
    }

}
