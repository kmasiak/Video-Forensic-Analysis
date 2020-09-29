package com.example.videoforensicexaminer.model;

import java.io.File;
import java.util.Objects;

public class VideoFile {
    private int icon;
    private String fileName;
    private File file;
    private String corpusId;
    private String recordingEnv;
    private String maskType = "";
    private boolean isMasked;


    public VideoFile() {
    }

    public VideoFile(int play_btn, String fileName, File file) {
        this.icon = play_btn;
        this.fileName = fileName;
        this.file = file;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com.example.videoforensicexaminer.model.VideoFile videoFile = (com.example.videoforensicexaminer.model.VideoFile) o;
        return Objects.equals(getFileName(), videoFile.getFileName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileName());
    }

    public VideoFile(int icon, String fileName, File file, String corpusId, String recordingEnv, boolean isMasked) {
        this.icon = icon;
        this.fileName = fileName;
        this.file = file;
        this.corpusId = corpusId;
        this.recordingEnv = recordingEnv;
    }
    //Constructor for VideoFile for use in V2 mask recordings
    public VideoFile(int icon, String fileName, File file, String corpusId, String recordingEnv, String maskType, boolean isMasked){
        this.icon = icon;
        this.fileName = fileName;
        this.file = file;
        this.corpusId = corpusId;
        this.recordingEnv = recordingEnv;
        this.maskType = maskType;
        this.isMasked = isMasked;
    }

    public String getCorpusId() {
        return corpusId;
    }

    public void setCorpusId(String corpusId) {
        this.corpusId = corpusId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public VideoFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRecordingEnv() {
        return recordingEnv;
    }

    public void setRecordingEnv(String recordingEnv) {
        this.recordingEnv = recordingEnv;
    }

    public String getMaskType() { return maskType;}

    public void setMaskType(String maskType) {
        this.maskType = maskType;
    }

    public void setIsMasked(boolean isMasked) {this.isMasked = isMasked;}

    public boolean getIsMasked() {return isMasked;}

}
