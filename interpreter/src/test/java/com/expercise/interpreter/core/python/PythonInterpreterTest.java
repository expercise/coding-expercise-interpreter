package com.expercise.interpreter.core.python;

import com.expercise.interpreter.core.BaseInterpreterTest;
import com.expercise.interpreter.core.Interpreter;
import org.mockito.InjectMocks;

public class PythonInterpreterTest extends BaseInterpreterTest {

    @InjectMocks
    private PythonInterpreter interpreter;

    @Override
    protected Interpreter getInterpreter() {
        return interpreter;
    }

    @Override
    protected String getFilePath(String fileName) {
        return "/python/" + fileName;
    }

}