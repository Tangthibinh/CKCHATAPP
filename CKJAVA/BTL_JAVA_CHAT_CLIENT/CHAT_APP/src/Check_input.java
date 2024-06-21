package src;

public class Check_input {

    public Check_input() {

    }

    public boolean Email(String Email) {
        boolean check = true;
        if (Email.length() < 7) {
            check = false;
        } else {
            if (Email.contains(".") && Email.contains("@")) {
                if (Email.contains(" ")) {
                    check = false;
                } else {
                    check = true;
                }
            } else {
                check = false;
            }
        }
        return check;
    }

    public boolean Password(String Pass) {
        boolean check = true;
        if (Pass.length() < 9) {
            check = false;
        } else {
            if (Pass.contains(" ")) {
                check = false;
            } else {
                check = true;
            }

        }
        return check;
    }
}
