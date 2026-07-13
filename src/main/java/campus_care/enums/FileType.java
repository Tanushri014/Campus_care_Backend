package campus_care.enums;

import lombok.Getter;

@Getter
public enum FileType {

    COMPLAINT("studentComplaintImages"),

    LOST_FOUND("lostAndFoundImages"),

    NOTICE("notices");

    private final String folderName;

    FileType(String folderName) {
        this.folderName = folderName;
    }
}