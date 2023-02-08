package demos.java17.sealedclasses;

public sealed interface HasName permits Employee, Consumer, HasNameAndYearOfBirth {
}
