package de.tt.zpaqgui;

import de.tt.zpaqgui.parts.FileSystemPart;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import javax.inject.Singleton;
import java.net.URL;
import java.text.DecimalFormat;

@Creatable
@Singleton
public class UIUtility {

    public static final String[][] FILTER = new String[][]{{"*.zpaq", "*"}, {"ZPAQ Archives", "All Files"}};

    private static final DecimalFormat DF = new DecimalFormat("#.##");
    private static Image folder;
    private static Image up;
    private static Image refresh;

    private static final String[] threadarray;

    static {
        final int corecount = Runtime.getRuntime().availableProcessors();

        threadarray = getArrayOfNumbers(corecount);

        final Bundle bundle = FrameworkUtil.getBundle(FileSystemPart.class);
        final URL url = FileLocator.find(bundle, new Path("icons/16/folder.png"), null);
        final ImageDescriptor imageDcr = ImageDescriptor.createFromURL(url);
        folder = imageDcr.createImage();

        final URL upurl = FileLocator.find(bundle, new Path("icons/16/go-up.png"), null);
        final ImageDescriptor upDcr = ImageDescriptor.createFromURL(upurl);
        up = upDcr.createImage();

        final URL refreshurl = FileLocator.find(bundle, new Path("icons/16/view-refresh.png"), null);
        final ImageDescriptor refreshDcr = ImageDescriptor.createFromURL(refreshurl);
        refresh = refreshDcr.createImage();
    }

    public static String createReadableSize(long size) {
        try {
            if (size < 1024) {
                return size + " B";
            } else if (size < 1024l * 1024l) {
                return String.valueOf(DF.format((double) size / 1024d)) + " KiB";
            } else if (size < 1024l * 1024l * 1024l) {
                return String.valueOf(DF.format((double) size / (1024d * 1024d))) + " MiB";
            } else if (size < 1024l * 1024l * 1024l * 1024l) {
                return String.valueOf(DF.format((double) size / (1024d * 1024d * 1024d))) + " GiB";
            } else {
                return String.valueOf(DF.format((double) size / (1024d * 1024d * 1024d * 1024d))) + " TiB";
            }
        } catch (ArithmeticException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

    public static Image getFolder() {
        return folder;
    }

    public static Image getUp() {
        return up;
    }

    public static String createReadableRatio(float ratio) {
        final double myfloat = 100 * ratio;

        return String.valueOf(DF.format(myfloat)) + "%";
    }

    public static String[] createVersionArray(final short version) {
        return getArrayOfNumbers(version);
    }

    private static String[] getArrayOfNumbers(final int count) {
        final String[] array = new String[count];

        for (int i = 0; i < count; ) {
            array[i++] = String.valueOf(i);
        }
        return array;
    }

    public static String[] getThreadCountsArray() {
        return threadarray;
    }

    public static Image getRefresh() {
        return refresh;
    }
}
