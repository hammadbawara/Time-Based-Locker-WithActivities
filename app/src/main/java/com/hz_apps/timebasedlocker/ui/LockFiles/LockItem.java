package com.hz_apps.timebasedlocker.ui.LockFiles;

import androidx.annotation.NonNull;

import java.io.File;
import java.time.LocalDateTime;

public class LockItem extends File {

    public final LocalDateTime fileLockTime;
    public final LocalDateTime fileUnlockTime;

    public LockItem(@NonNull String pathname, LocalDateTime fileLockTime, LocalDateTime fileUnlockTime) {
        super(pathname);
        this.fileLockTime = fileLockTime;
        this.fileUnlockTime = fileUnlockTime;
    }

    public LocalDateTime getFileLockTime() {
        return fileLockTime;
    }

    public LocalDateTime getFileUnlockTime() {
        return fileUnlockTime;
    }
}
