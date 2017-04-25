package com.lesuorac.wx.log;

import java.util.List;

public interface RsyslogParser<T> {

    /**
     * returns a list of T that have been decoded using the given rows
     *
     * @param rows
     * @return
     */
    List<T> parse(List<String> rows);
}
