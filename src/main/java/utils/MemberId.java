package utils;

//Helper class for adding new members to assignments
public class MemberId {
    private String id;
    private Integer assignmentId;

    public MemberId(String id, Integer assignmentId) {
        this.id = id;
        this.assignmentId = assignmentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Integer assignmentId) {
        this.assignmentId = assignmentId;
    }
}
