package Api;

public class UnSuccessfulRegister {
    private String error;

    public UnSuccessfulRegister(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
