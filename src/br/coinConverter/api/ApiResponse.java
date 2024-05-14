package br.coinConverter.api;

import com.google.gson.annotations.SerializedName;

public record ApiResponse(
        @SerializedName("result") String result,
        @SerializedName("time_last_update_utc") String lastUpdate,
        @SerializedName("base_code") String baseCode,
        @SerializedName("target_code") String targetCode,
        @SerializedName("conversion_rate") double conversionRate,
        @SerializedName("error-type") String errorType){
}
