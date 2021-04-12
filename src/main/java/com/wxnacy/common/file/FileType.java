package com.wxnacy.common.file;

public enum FileType {
    IMAGE(new String[]{"jpg", "png", "gif"}), VIDEO(new String[]{"mp4", "mkv"});
    private String[] names;
    private FileType(String[] names) {
        this.names = names;
    }

    public String[] getNames() {
        return names;
    }
}
