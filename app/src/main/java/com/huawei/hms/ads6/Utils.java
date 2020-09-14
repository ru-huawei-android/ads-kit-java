package com.huawei.hms.ads6;

import com.huawei.hms.ads.AdParam;

public class Utils {
    /*
    Details: https://developer.huawei.com/consumer/en/doc/development/HMS-References/ads-api-adparam-errorcode
    */

    public static final String getErrorMessage(int errCode) {
        switch (errCode) {
            case AdParam.ErrorCode.INNER:
                return "Internal error";
            case AdParam.ErrorCode.INVALID_REQUEST:
                return "The ad request is invalid due to causes, such as not setting the ad slot ID or invalid banner ad size.";
            case AdParam.ErrorCode.NETWORK_ERROR:
                return "The ad request fails due to a network connection error.";
            case AdParam.ErrorCode.NO_AD:
                return "The ad request is successful, but the server does not return any available ad material.";
            case AdParam.ErrorCode.AD_LOADING:
                return "The ad is being requested and cannot be requested again.";
            case AdParam.ErrorCode.LOW_API:
                return "The API version is not supported by the HUAWEI Ads SDK.";
            case AdParam.ErrorCode.BANNER_AD_EXPIRE:
                return "The banner ad has expired.";
            case AdParam.ErrorCode.BANNER_AD_CANCEL:
                return "The banner ad task is removed.";
            default:
                return "Unknown error";
        }
    }
}
