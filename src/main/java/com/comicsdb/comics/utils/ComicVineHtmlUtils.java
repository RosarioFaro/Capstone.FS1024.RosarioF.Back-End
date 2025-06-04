package com.comicsdb.comics.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComicVineHtmlUtils {
    
    private static final Map<String, String> TYPE_PATH_MAP = new HashMap<>();
    static {
        TYPE_PATH_MAP.put("4050", "volumes");
        TYPE_PATH_MAP.put("4010", "publisher");
        TYPE_PATH_MAP.put("4005", "character");
        TYPE_PATH_MAP.put("4060", "team");
        TYPE_PATH_MAP.put("4015", "concept");
        TYPE_PATH_MAP.put("4000", "issue");
        TYPE_PATH_MAP.put("4051", "series");
        TYPE_PATH_MAP.put("4040", "person");
    }
    
    private static final Pattern COMICVINE_LINK_PATTERN = Pattern.compile(
            "href\\s*=\\s*['\"](?:https?://(?:comicvine\\.gamespot\\.com|www\\.comicvine\\.com))?/([\\w-]+)/([0-9]{4,5})-([0-9]+)(?:/[^'\"]*)?/?['\"]",
            Pattern.CASE_INSENSITIVE
    );
    
    public static String remapComicVineLinks(String description) {
        if (description == null) return "";
        Matcher matcher = COMICVINE_LINK_PATTERN.matcher(description);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String slug = matcher.group(1);
            String code = matcher.group(2);
            String id = matcher.group(3);
            String path = TYPE_PATH_MAP.getOrDefault(code, "comicvine");
            matcher.appendReplacement(sb, "href=\"/" + path + "/" + id + "\"");
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    
}
