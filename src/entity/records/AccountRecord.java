package entity.records;

import enums.ACCTYPE;

public record AccountRecord(
        Integer accNo,
        Long nationalId,
        String username,
        String accName,
        ACCTYPE acctype
) {
    @Override
    public String toString() {
        return
                "Account No :" + accNo +
                ", National Id :" + nationalId +
                ", User's name :" + username +
                ", Account name :" + accName +
                ", Account type :"+ acctype  ;
    }
}
