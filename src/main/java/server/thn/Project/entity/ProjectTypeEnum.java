package server.thn.Project.entity;

public enum ProjectTypeEnum {
    양산개발(0),
    선행개발(1)
    ;

    private final Integer label;

    ProjectTypeEnum (Integer label){
        this.label = label;
    }

    public Integer label() {
        return label;
    }

}

