package model;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 140179 on 2015-06-08.
 */
public class Buildnote {
    private User user;
    private Issue taskIssue;
    private Issue sqlIssue;

    public Issue getSqlIssue() {
        return sqlIssue;
    }

    public void setSqlIssue(Issue sqlIssue) {
        this.sqlIssue = sqlIssue;
    }

    public Issue getTaskIssue() {
        return taskIssue;
    }

    public void setTaskIssue(Issue taskIssue) {
        this.taskIssue = taskIssue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 프로젝트
     * project-0103 - 운영배포관리(QA)
     */
    private String projectId;

    /**
     * 유형
     * 13 - 정기배포 (default)
     * 14 - 비정기배포
     * 15 - 긴급배포
     * 16 - 데이터수정
     */
    private int trackerId;

    /**
     * 상태
     * 1 - 오픈
     * 12 - 재오픈
     * 19 - 승인완료
     * 6 - 중단
     * 5 - 완료
     */
    private int statusId;

    /**
     * 우선순위
     * 1 - 낮음
     * 2 - 보통 (default)
     * 3 - 높음
     * 4 - 긴급
     * 5 - 즉시
     */
    private int priorityId;

    /**
     * 제목
     */
    private String subject;

    /**
     * 설명
     */
    private String description;

    /**
     * 담당자
     */
    private String assignedToId;

    /**
     * custom_field 26
     * 변경작업시스템
     */
    private String[] changeSystems;

    /**
     * custom_field 29
     * 기획자
     */
    private List<Author> requesters;

    /**
     * custom_field 37
     * 배포시스템
     */
    private String[] distSystems;

    /**
     * 기획자 테스트 요구사항
     */
    private String[] requirements;

    /**
     * 소스 파일
     */
    private String[] files;

    /**
     * custom_field 38
     * 배포일자 (YYYY-MM-DD)
     */
    private String dueDate;

    /**
     * 개발시작일 (YYYY-MM-DD)
     */
    private String devStartDate;

    /**
     * QA반영일 (YYYY-MM-DD)
     */
    private String qaDate;

    /**
     * 배포타입
     */
    private String distType;

    public String getDistType() {
        return distType;
    }

    public void setDistType(String distType) {
        this.distType = distType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public int getTrackerId() {
        return trackerId;
    }

    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(int priorityId) {
        this.priorityId = priorityId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        this.assignedToId = assignedToId;
    }

    public String[] getChangeSystems() {
        return changeSystems;
    }

    public void setChangeSystems(String[] changeSystems) {
        this.changeSystems = changeSystems;
    }

    public List<Author> getRequesters() {
        return requesters;
    }

    public void setRequesters(List<Author> requesters) {
        this.requesters = requesters;
    }

    public String[] getDistSystems() {
        return distSystems;
    }

    public void setDistSystems(String[] distSystems) {
        this.distSystems = distSystems;
    }

    public String[] getRequirements() {
        return requirements;
    }

    public void setRequirements(String[] requirements) {
        this.requirements = requirements;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDevStartDate() {
        return devStartDate;
    }

    public void setDevStartDate(String devStartDate) {
        this.devStartDate = devStartDate;
    }

    public String getQaDate() {
        return qaDate;
    }

    public void setQaDate(String qaDate) {
        this.qaDate = qaDate;
    }

    public String getBuildnoteIssueString() {
        Integer[] requestersId = new Integer[this.requesters.size()];
        String[] requestersName = new String[this.requesters.size()];
        for (int i=0; i < this.requesters.size(); i++) {
            requestersId[i] = this.requesters.get(i).getId();
            requestersName[i] = this.requesters.get(i).getName();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"issue\":{");
        sb.append("\"project_id\":\"" + this.projectId + "\",");
        sb.append("\"tracker_id\":" + this.trackerId + ",");
        sb.append("\"status_id\":" + this.statusId + ",");
        sb.append("\"priority_id\":" + this.priorityId + ",");
        sb.append("\"subject\":\"" + this.subject + "\",");
        sb.append("\"description\":\"");
        sb.append("h2. 개요\\n\\n");
        sb.append("| 요청자 | " + StringUtils.join(requestersName, ", ") + " |\\n");
        sb.append("| 기획 담당자 | " + StringUtils.join(requestersName, ", ") + " |\\n");
        sb.append("| 개발/빌드 담당 | " + user.getFirstname() + " " + user.getLastname() + " |\\n");
        sb.append("| 개발 기간 | " + this.devStartDate + " ~ " + this.dueDate + " |\\n");
        sb.append("| QA 반영 일자 | " + this.qaDate + " |\\n");
        sb.append("| 대상 서비스 | " + StringUtils.join(distSystems, ", ") + " |\\n\\n");
        sb.append("h2. 개발 SPEC (기능 추가 및 삭제/변경 내역)\\n\\n");
        sb.append("| Redmine key | #" + taskIssue.getId() + " |\\n");
        sb.append("| 내역 | " + taskIssue.getSubject() + " |\\n\\n");
        sb.append("h2. 기획자 테스트 요구 사항\\n\\n");
        sb.append("| " + StringUtils.join(requirements, "|\\n|") + " |\\n\\n");
        sb.append("h2. 영향 받는 시스템 및 기능 (Dependency Module)\\n\\n");
        sb.append("| 담당자 확인 | |\\n");
        sb.append("| 시스템명 | |\\n");
        sb.append("| 영향 받는 기능 / 영역 | |\\n\\n");
        sb.append("h2. SQL 검수 완료 여부\\n\\n");
        sb.append("| SQL 검수 완료 확인 (.xml 소스 반영 시) | " + ((sqlIssue != null) ? sqlIssue.getStatus().getName() : "") + " |\\n");
        sb.append("| SQL 검수 요청 Redmine Key | #" + ((sqlIssue != null) ? sqlIssue.getId() : "") + " |\\n\\n");
        sb.append("h2. 소스 파일 경로\\n\\n");
        sb.append("| " + StringUtils.join(files, " |\\n| ") + " |\\n\\n");
        sb.append("h2. 배포 요구 사항\\n\\n");
        sb.append("| 배포 유형 | " + this.distType + " |\\n");
        sb.append("| QA WAR 버전 |  |\\n");
        sb.append("| 유의사항 |  |\\n\",");
        sb.append("\"assigned_to_id\":\"" + user.getId() + "\",");
        sb.append("\"custom_fields\":[{");
        sb.append("\"id\":\"26\",");
        sb.append("\"value\":[\"" + StringUtils.join(this.changeSystems, "\",\"") + "\"]");
        sb.append("},{");
        sb.append("\"id\":\"29\",");
        sb.append("\"value\":[\"" + StringUtils.join(requestersId, "\",\"") + "\"]");
        sb.append("},{");
        sb.append("\"id\":\"37\",");
        sb.append("\"value\":[\"" + StringUtils.join(this.distSystems, "\",\"") + "\"]");
        sb.append("},{");
        sb.append("\"id\":\"38\",");
        sb.append("\"value\":\"" + this.dueDate + "\"");
        sb.append("}]");
        sb.append("}}");

        return sb.toString();
    }

    public Buildnote() {
        this.projectId = "project-0103";
        this.trackerId = 13;
        this.statusId = 1;
        this.priorityId = 2;
        this.subject = "";
        this.description = "";
        this.assignedToId = "";
        this.changeSystems = new String[]{};
        this.requesters = new ArrayList<Author>();
        this.distSystems = new String[]{};
        this.requirements = new String[]{};
        this.files = new String[]{};
        this.dueDate = "";
    }

    @Override
    public String toString() {
        return "Buildnote{" +
                "projectId='" + projectId + '\'' +
                ", trackerId=" + trackerId +
                ", statusId=" + statusId +
                ", priorityId=" + priorityId +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", assignedToId='" + assignedToId + '\'' +
                ", changeSystems=" + Arrays.toString(changeSystems) +
                ", requesters=" + requesters.toString() +
                ", distSystems=" + Arrays.toString(distSystems) +
                ", requirements=" + Arrays.toString(requirements) +
                ", files=" + Arrays.toString(files) +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
