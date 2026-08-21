package com.acsousa.gerenciador_de_rotinas.utils.enums;

import com.acsousa.gerenciador_de_rotinas.enums.DescribableEnum;
import com.acsousa.gerenciador_de_rotinas.records.EnumRecord;

import java.util.ArrayList;
import java.util.List;

public class EnumUtil {

    public static <E extends Enum<E>& DescribableEnum> List<EnumRecord> convertEnumToList(Class<E> enumClass) {
        List<EnumRecord> enumList = new ArrayList<>();
        E[] enumValues = enumClass.getEnumConstants();

        if(enumValues != null) {
            for (int i = 0; i < enumValues.length; i++) {
                enumList.add(new EnumRecord(i, enumValues[i].name(), enumValues[i].getDescription()));
            }
        }

        return enumList;
    }
}
