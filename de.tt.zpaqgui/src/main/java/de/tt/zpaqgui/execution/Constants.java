package de.tt.zpaqgui.execution;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String[] FRAGMENT_SIZES =
            {"1 KiB", "2 KiB", "4 KiB", "8 KiB", "16 KiB", "32 KiB", "64 KiB", "128 KiB", "256 KiB", "512 KiB", "1 MiB",
                    "2 MiB", "4 MiB", "8 MiB", "16 MiB", "32 MiB", "64 MiB", "128 MiB", "256 MiB"};
    public static final String[] BLOCK_SIZES =
            {"1 MiB", "2 MiB", "4 MiB", "8 MiB", "16 MiB", "32 MiB", "64 MiB", "128 MiB", "256 MiB", "512 MiB", "1 GiB", "2 GiB"};
    public static final int[] COMPRESSION_MEMORY_USAGE = {0, 128, 450, 450, 550, 850};
    public static final int[] DECOMPRESSION_MEMORY_USAGE = {0, 32, 128, 400, 550, 850};
    public static final int[] DEFAULT_BLOCK_SIZE = {4, 4, 6, 6, 6, 6};
    public static final int DEDUPLICATION_COMPRESSION_MEMORY = 1024;
    public static final int DEDUPLICATION_DECOMPRESSION_MEMORY = 512;
    public static final int DEFAULT_FRAGMENT_SIZE = 6;
    public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
