package com.tracenull.service;

import com.tracenull.po.ResultDto;

public interface VideoParseUrlService {
    ResultDto dyParseUrl(String redirectUrl) throws Exception;

    ResultDto hsParseUrl(String redirectUrl) throws Exception;

    ResultDto QMParseUrl(String redirectUrl) throws Exception;
}
