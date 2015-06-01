package com.ataulm.wutson.view;

final class AspectRatio {

    private final int aspectWidth;
    private final int aspectHeight;

    public static AspectRatio parse(String aspectRatio) {
        if (aspectRatio == null || aspectRatio.trim().isEmpty() || !aspectRatio.contains(":")) {
            complainLikeHeckAboutTheFormat();
        }
        String[] parts = splitAspectRatioIntoParts(aspectRatio);
        return parseAspectRatioParts(parts[0], parts[1]);
    }

    private static String[] splitAspectRatioIntoParts(String aspectRatio) {
        String[] parts = aspectRatio.trim().split(":");
        if (parts.length != 2) {
            complainLikeHeckAboutTheFormat();
        }
        return parts;
    }

    private static AspectRatio parseAspectRatioParts(String partWidth, String partHeight) {
        int aspectWidth = 0;
        int aspectHeight = 0;
        try {
            aspectWidth = Integer.parseInt(partWidth);
            aspectHeight = Integer.parseInt(partHeight);
        } catch (NumberFormatException e) {
            complainLikeHeckAboutTheFormat();
        }
        return new AspectRatio(aspectWidth, aspectHeight);
    }

    private static AspectRatio complainLikeHeckAboutTheFormat() {
        throw new IllegalArgumentException("aspectRatio must be in the form 'width:height', e.g. `app:aspectRatio=\"16:9\"`");
    }

    private AspectRatio(int aspectWidth, int aspectHeight) {
        this.aspectWidth = aspectWidth;
        this.aspectHeight = aspectHeight;
    }

    public int getAspectWidth() {
        return aspectWidth;
    }

    public int getAspectHeight() {
        return aspectHeight;
    }

}
