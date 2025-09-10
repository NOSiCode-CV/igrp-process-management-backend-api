package cv.igrp.platform.process.management.shared.domain.exceptions;

import java.util.function.Function;
import java.util.function.Supplier;

public class ExceptionFunctions {

    public static final Function<String, Supplier<IgrpResponseStatusException>> buildExceptionNotFound =
            msg -> () -> IgrpResponseStatusException.notFound(msg);

    private ExceptionFunctions() {
    }
}
