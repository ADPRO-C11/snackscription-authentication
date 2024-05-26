package snackscription.authentication.enums;

import lombok.Getter;

@Getter
public enum UserType {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    private UserType(String value){
        this.value = value;
    }

    public static boolean contains(String param){
        for(UserType userType: UserType.values()){
            if(userType.name().equals(param)){
                return true;
            }
        }
        return false;
    }
}
