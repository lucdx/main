package seedu.addressbook.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import seedu.addressbook.data.AddressBook;
import seedu.addressbook.data.ExamBook;
import seedu.addressbook.data.exception.IllegalValueException;
import seedu.addressbook.storage.jaxb.AdaptedAddressBook;
import seedu.addressbook.storage.jaxb.AdaptedExamBook;

/**
 * Represents the file used to store address book data.
 */
public class StorageFile extends Storage {

    /** Default file path used if the user doesn't provide the file name. */
    public static final String DEFAULT_STORAGE_FILEPATH = "addressbook.txt";

    /** Default file path used if the user doesn't provide the file name. */
    public static final String DEFAULT_EXAMS_FILEPATH = "exams.txt";

    public final Path path;
    public final Path pathExam;

    private final JAXBContext jaxbContext;
    private final JAXBContext jaxbContext2;

    /* Note: Note the use of nested classes below.
     * More info https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html
     */

    /**
     * Signals that the given file path does not fulfill the storage filepath constraints.
     */
    public static class InvalidStorageFilePathException extends IllegalValueException {
        public InvalidStorageFilePathException(String message) {
            super(message);
        }
    }

    /**
     * Signals that there is some jaxb initialisation error.
     */
    public static class InvalidInitialisationException extends IllegalValueException {
        public InvalidInitialisationException(String message) {
            super(message);
        }
    }


    /**
     * @throws InvalidStorageFilePathException if the default path is invalid
     */
    public StorageFile() throws InvalidStorageFilePathException,
            InvalidInitialisationException {
        this(DEFAULT_STORAGE_FILEPATH, DEFAULT_EXAMS_FILEPATH);
    }

    /**
     * @throws InvalidStorageFilePathException if the given file path is invalid
     */
    public StorageFile(String filePath, String filePathExam)
            throws InvalidStorageFilePathException,
                InvalidInitialisationException {
        try {
            jaxbContext = JAXBContext.newInstance(AdaptedAddressBook.class);
            jaxbContext2 = JAXBContext.newInstance(AdaptedExamBook.class);
        } catch (JAXBException jaxbe) {
            throw new InvalidInitialisationException("jaxb initialisation error");
        }

        path = Paths.get(filePath);
        if (!isValidPath(path)) {
            throw new InvalidStorageFilePathException("Storage file should end with '.txt'");
        }
        pathExam = Paths.get(filePathExam);
        if (!isValidPath(pathExam)) {
            throw new InvalidStorageFilePathException("Exam file should end with '.txt'");
        }
    }

    /**
     * Returns true if the given path is acceptable as a storage file.
     * The file path is considered acceptable if it ends with '.txt'
     */
    private static boolean isValidPath(Path filePath) {
        return filePath.toString().endsWith(".txt");
    }

    /**
     * Saves all data to this storage file.
     *
     * @throws StorageOperationException if there were errors converting and/or storing data to file.
     */
    public void save(AddressBook addressBook) throws StorageOperationException {

        /* Note: Note the 'try with resource' statement below.
         * More info: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
         */
        try (final Writer fileWriter = new BufferedWriter(new FileWriter(path.toFile()))) {
            final AdaptedAddressBook toSave = new AdaptedAddressBook(addressBook);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(toSave, fileWriter);
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + path + " error: " + ioe.getMessage());
        } catch (JAXBException jaxbe) {
            throw new StorageOperationException("Error converting address book into storage format");
        }
    }

    /**
     * Loads data from this storage file.
     *
     * @throws StorageOperationException if there were errors reading and/or converting data from file.
     */
    public AddressBook load() throws StorageOperationException {
        try (final Reader fileReader = new BufferedReader(new FileReader(path.toFile()))) {
            final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            final AdaptedAddressBook loaded = (AdaptedAddressBook) unmarshaller.unmarshal(fileReader);
            // manual check for missing elements
            if (loaded.isAnyRequiredFieldMissing()) {
                throw new StorageOperationException("File data missing some elements");
            }
            return loaded.toModelType();

            /* Note: Here, we are using an exception to create the file if it is missing. However, we should minimize
             * using exceptions to facilitate normal paths of execution. If we consider the missing file as a 'normal'
             * situation (i.e. not truly exceptional) we should not use an exception to handle it.
             */

            // create empty file if not found
        } catch (FileNotFoundException fnfe) {
            final AddressBook empty = new AddressBook();
            save(empty);
            return empty;

            // other errors
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + path);
        } catch (JAXBException jaxbe) {
            throw new StorageOperationException("Error parsing file data format");
        } catch (IllegalValueException ive) {
            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
        }
    }

    public String getPath() {
        return path.toString();
    }

    public String getPathExam() {
        return pathExam.toString();
    }

    /**
     * Saves all data to this storage file.
     *
     * @throws StorageOperationException if there were errors converting and/or storing data to file.
     */
    public void saveExam(ExamBook examBook) throws StorageOperationException {

        /* Note: Note the 'try with resource' statement below.
         * More info: https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
         */
        try (final Writer fileWriter = new BufferedWriter(new FileWriter(pathExam.toFile()))) {
            final AdaptedExamBook toSave = new AdaptedExamBook(examBook);
            final Marshaller marshaller = jaxbContext2.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(toSave, fileWriter);
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + pathExam + " error: " + ioe.getMessage());
        } catch (JAXBException jaxbe) {
            throw new StorageOperationException("Error converting exam book into storage format");
        }
    }

    /**
     * Loads data from this storage file.
     *
     * @throws StorageOperationException if there were errors reading and/or converting data from file.
     */
    public ExamBook loadExam() throws StorageOperationException {
        try (final Reader fileReader = new BufferedReader(new FileReader(pathExam.toFile()))) {
            final Unmarshaller unmarshaller = jaxbContext2.createUnmarshaller();
            final AdaptedExamBook loaded = (AdaptedExamBook) unmarshaller.unmarshal(fileReader);
            // manual check for missing elements
            if (loaded.isAnyRequiredFieldMissing()) {
                throw new StorageOperationException("File data missing some elements");
            }
            return loaded.toModelType();

            /* Note: Here, we are using an exception to create the file if it is missing. However, we should minimize
             * using exceptions to facilitate normal paths of execution. If we consider the missing file as a 'normal'
             * situation (i.e. not truly exceptional) we should not use an exception to handle it.
             */

            // create empty file if not found
        } catch (FileNotFoundException fnfe) {
            final ExamBook empty = new ExamBook();
            saveExam(empty);
            return empty;

            // other errors
        } catch (IOException ioe) {
            throw new StorageOperationException("Error writing to file: " + pathExam);
        } catch (JAXBException jaxbe) {
            throw new StorageOperationException("Error parsing file data format");
        } catch (IllegalValueException ive) {
            throw new StorageOperationException("File contains illegal data values; data type constraints not met");
        }

    }

}