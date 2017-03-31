package mkolaczek.elm.jps;

public class ErrorJson {

    public final String tag;
    public final String overview;
    public final Region subregion;
    public final String details;
    public final Region region;
    public final String type;
    public final String file;

    public ErrorJson(String tag,
                     String overview,
                     Region subregion,
                     String details,
                     Region region,
                     String type,
                     String file) {
        this.tag = tag;
        this.overview = overview;
        this.subregion = subregion;
        this.details = details;
        this.region = region;
        this.type = type;
        this.file = file;
    }

    public static class Region {
        public final Location start;
        public final Location end;

        public Region(Location start, Location end) {
            this.start = start;
            this.end = end;
        }
    }

    public static class Location {
        public final int line;
        public final int column;

        public Location(int line, int column) {
            this.line = line;
            this.column = column;
        }
    }
}
