package clausEnterprises.crm.consts;

import clausEnterprises.crm.consts.enums.RejectionReason;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessagesConst {
    private final static String SAME_EMAIL_REJECTION = "Hello! Claus Enterprises just got a message from you regarding " +
            "getting a gift for a good behaviour. \n" +
            "Unfortunately, we could not get sure that the parent's email definitely belongs to them. " +
            "You can try once again, but make sure that your parent's " +
            "email is valid this time. \nWe're sorry, hoping to hear you from you in future\nClaus Enterprises (c)";
    private final static String BAD_BEHAVIOUR_REJECTION = "Hello! Claus Enterprises just got a message from you regarding " +
            "getting a gift for a good behaviour. \n" +
            "Unfortunately, a little birdie told us that you behaved bad this year, so we can't send you a gift. " +
            "\nWe're sorry, hoping to hear you from you in future\nClaus Enterprises (c)";
    public final static String SECOND_TRY_TO_PARENT = "Hello! Claus Enterprises just got a response from you regarding " +
            "your child's behaviour this year. \n" +
            "Unfortunately, we could not get sure your position about it. Can you please fill this form once again, so we" +
            " could clearly understand your child's behaviour  " +
            "Here's the link to the form - https://forms.gle/uB2a37DvVSk5teUYA" +
            "\nClaus Enterprises (c)";
    private final static String WRONG_EMAIL_REJECTION = "Hello! Claus Enterprises just got a message from you regarding " +
            "getting a gift for a good behaviour. \n" +
            "Unfortunately, we could not able to check your parent's email. " +
            "You can try once again, but make sure that your parent's " +
            "email is valid this time. \nWe're sorry, hoping to hear you from you in future\nClaus Enterprises (c)";
    private final static String ALREADY_REGISTERED_REJECTION = "Hello! Claus Enterprises just got a message from you regarding " +
            "getting a gift for a good behaviour. \n" +
            "Unfortunately, we can't send you a gift considering the fac    t that you already got a gift from us this year. " +
            "You can try later.\nWe're sorry, hoping to hear you from you in future\nClaus Enterprises (c)";
    public static Map<RejectionReason, String> REJECTION_MAP = Stream.of(new AbstractMap.SimpleImmutableEntry<>
                            (RejectionReason.SAME_EMAIL, SAME_EMAIL_REJECTION),
                    new AbstractMap.SimpleImmutableEntry<>(RejectionReason.WRONG_EMAIL, WRONG_EMAIL_REJECTION),
                    new AbstractMap.SimpleImmutableEntry<>(RejectionReason.ALREADY_REGISTERED, ALREADY_REGISTERED_REJECTION),
                    new AbstractMap.SimpleImmutableEntry<>(RejectionReason.BAD_BEHAVIOUR, BAD_BEHAVIOUR_REJECTION))
            .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));

}
