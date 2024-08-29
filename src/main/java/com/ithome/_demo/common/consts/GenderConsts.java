package com.ithome._demo.common.consts;

import lombok.Data;

public class GenderConsts {
    public enum Gender {
        MALE("Male", "男"),
        FEMALE("Female", "女");

        private String code;
        private String value;

        Gender(String code, String value) {
            this.code = code;
            this.value = value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
