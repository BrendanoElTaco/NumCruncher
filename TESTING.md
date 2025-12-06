# Testing NumCruncher

This project is a simple Swing application without an automated test harness. You can still verify behavior by compiling and running the UI locally.

## Prerequisites
- JDK 8 or newer

## Compile
Use `javac` to compile all sources into a temporary `out` directory:

```bash
javac -d out src/*.java
```

If compilation succeeds, the `out` directory will contain the generated class files.

## Run
Launch the calculator from the compiled classes with the Swing UI:

```bash
java -cp out Calculator
```

The window should open with the same behavior described in the UI. You can now manually exercise numeric entry, operations, trigonometric functions, and secondary mode toggles to confirm the refactored handlers behave as expected.

## Manual verification ideas
- Enter multi-digit numbers and confirm they display correctly.
- Chain basic operations (e.g., `2 + 3 × 4 =`) to ensure cumulative results are preserved.
- Toggle secondary functions and verify button labels update and inverse behaviors are executed.
- Use special constants (π, e) and functions (square root, logarithms) and check results against a trusted calculator.
- Validate error handling such as division by zero or invalid logarithm bases.
