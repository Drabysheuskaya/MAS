package com.danven.web_library.domain.book;

public interface IDiskBook {

    void setDurationInHours(double durationInHours);

    double getDurationInHours();

    void setDiskFormat(DiskFormat diskFormat);

    DiskFormat getDiskFormat();
}
