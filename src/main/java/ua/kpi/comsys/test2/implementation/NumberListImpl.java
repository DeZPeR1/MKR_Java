/*
 * Copyright (c) 2014, NTUU KPI, Computer systems department and/or its affiliates. All rights reserved.
 * NTUU KPI PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */

/**
 *
 *
 @author Шмигельський Ілля Богданович, Група ІС-31, № варіанту: 27
 *
 */

package ua.kpi.comsys.test2.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import ua.kpi.comsys.test2.NumberList;

/**
 * Custom implementation of INumberList interface.
 * Has to be implemented by each student independently.
 *
 * @author Student Name, Group, Record Book Number: 27
 * @version 1.0
 */
public class NumberListImpl implements NumberList {

    /**
     * Вузол для лінійного двонаправленого списку
     */
    private static class Node {
        byte data;
        Node prev;
        Node next;

        Node(byte data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final int base = 8; // Вісімкова система числення

    /**
     * Default constructor. Returns empty <tt>NumberListImpl</tt>
     */
    public NumberListImpl() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * from file, defined in string format.
     *
     * @param file - file where number is stored.
     */
    public NumberListImpl(File file) {
        this();
        if (file == null || !file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            if (line != null) {
                // Конвертуємо десятковий рядок у вісімкове представлення
                String octalString = decimalToOctal(line.trim());
                for (int i = 0; i < octalString.length(); i++) {
                    char c = octalString.charAt(i);
                    if (c >= '0' && c <= '7') {
                        add(Byte.parseByte(String.valueOf(c)));
                    }
                }
            }
        } catch (IOException e) {
            // Якщо файл не знайдено або помилка читання, залишаємо список порожнім
        } catch (NumberFormatException e) {
            // Невірний формат числа, залишаємо список порожнім
        }
    }

    /**
     * Constructs new <tt>NumberListImpl</tt> by <b>decimal</b> number
     * in string notation.
     *
     * @param value - number in string notation.
     */
    public NumberListImpl(String value) {
        this();
        if (value == null || value.trim().isEmpty()) {
            return;
        }

        // Перевіряємо, чи рядок містить тільки цифри
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (!Character.isDigit(c)) {
                // Невірний символ, залишаємо список порожнім
                clear();
                return;
            }
        }

        // Конвертуємо десятковий рядок у вісімкове представлення
        String octalString = decimalToOctal(value.trim());
        for (int i = 0; i < octalString.length(); i++) {
            char c = octalString.charAt(i);
            add(Byte.parseByte(String.valueOf(c)));
        }
    }

    /**
     * Конвертує десятковий рядок у вісімковий
     */
    private String decimalToOctal(String decimalStr) {
        if (decimalStr.isEmpty()) return "0";

        // Проста конвертація через BigInteger
        java.math.BigInteger decimal = new java.math.BigInteger(decimalStr);
        return decimal.toString(8);
    }

    /**
     * Конвертує вісімковий рядок у десятковий
     */
    private String octalToDecimal(String octalStr) {
        if (octalStr.isEmpty()) return "0";

        try {
            java.math.BigInteger decimal = new java.math.BigInteger(octalStr, 8);
            return decimal.toString();
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    /**
     * Перетворює вісімковий список у десятковий рядок
     */
    private String listToOctalString() {
        if (size == 0) return "0";

        StringBuilder sb = new StringBuilder();
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            current = current.next;
        }

        // Видаляємо ведучі нулі
        String result = sb.toString();
        while (result.length() > 1 && result.charAt(0) == '0') {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * Saves the number, stored in the list, into specified file
     * in <b>decimal</b> scale of notation.
     *
     * @param file - file where number has to be stored.
     */
    public void saveList(File file) {
        if (file == null) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.print(toDecimalString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns student's record book number, which has 4 decimal digits.
     *
     * @return student's record book number.
     */
    public static int getRecordBookNumber() {
        return 27; // Ваш номер залікової книжки
    }

    /**
     * Returns new <tt>NumberListImpl</tt> which represents the same number
     * in other scale of notation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @return <tt>NumberListImpl</tt> in other scale of notation.
     */
    public NumberListImpl changeScale() {
        NumberListImpl result = new NumberListImpl();
        // Для варіанту 27, додаткова система - десяткова
        // Ми маємо повернути десяткове представлення

        String decimalString = toDecimalString();
        for (int i = 0; i < decimalString.length(); i++) {
            char c = decimalString.charAt(i);
            result.add(Byte.parseByte(String.valueOf(c)));
        }
        return result;
    }

    /**
     * Returns new <tt>NumberListImpl</tt> which represents the result of
     * additional operation, defined by personal test assignment.<p>
     *
     * Does not impact the original list.
     *
     * @param arg - second argument of additional operation
     *
     * @return result of additional operation.
     */
    public NumberListImpl additionalOperation(NumberList arg) {
        if (arg == null) {
            throw new NullPointerException("Argument cannot be null");
        }

        // Для варіанту 27: алгебраїчне та логічне OR двох чисел
        // Конвертуємо обидва числа в десяткові, виконуємо OR, потім назад у вісімкові

        String decimal1 = this.toDecimalString();
        String decimal2 = ((NumberListImpl)arg).toDecimalString();

        java.math.BigInteger bigInt1 = new java.math.BigInteger(decimal1);
        java.math.BigInteger bigInt2 = new java.math.BigInteger(decimal2);

        // Алгебраїчне OR для великих чисел
        java.math.BigInteger result = bigInt1.or(bigInt2);

        // Конвертуємо результат назад у вісімкову систему
        String octalResult = result.toString(8);

        NumberListImpl resultList = new NumberListImpl();
        for (int i = 0; i < octalResult.length(); i++) {
            char c = octalResult.charAt(i);
            resultList.add(Byte.parseByte(String.valueOf(c)));
        }

        return resultList;
    }

    /**
     * Returns string representation of number, stored in the list
     * in <b>decimal</b> scale of notation.
     *
     * @return string representation in <b>decimal</b> scale.
     */
    public String toDecimalString() {
        String octalStr = listToOctalString();
        return octalToDecimal(octalStr);
    }

    @Override
    public String toString() {
        return listToOctalString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NumberListImpl)) return false;

        NumberListImpl that = (NumberListImpl) o;
        if (this.size != that.size) return false;

        Node thisCurrent = this.head;
        Node thatCurrent = that.head;

        while (thisCurrent != null && thatCurrent != null) {
            if (thisCurrent.data != thatCurrent.data) return false;
            thisCurrent = thisCurrent.next;
            thatCurrent = thatCurrent.next;
        }

        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Byte)) return false;

        byte value = (Byte) o;
        Node current = head;
        while (current != null) {
            if (current.data == value) return true;
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Byte next() {
                if (!hasNext()) throw new NoSuchElementException();
                byte value = current.data;
                current = current.next;
                return value;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        // Не реалізовано за умовами завдання
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Byte e) {
        if (e == null) throw new NullPointerException();
        if (e < 0 || e >= base) throw new IllegalArgumentException("Digit must be between 0 and " + (base-1));

        Node newNode = new Node(e);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Byte)) return false;

        byte value = (Byte) o;
        Node current = head;

        while (current != null) {
            if (current.data == value) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                } else {
                    head = current.next;
                }

                if (current.next != null) {
                    current.next.prev = current.prev;
                } else {
                    tail = current.prev;
                }

                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!contains(obj)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Byte> c) {
        boolean modified = false;
        for (Byte b : c) {
            if (add(b)) modified = true;
        }
        return modified;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Byte> c) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();

        if (c.isEmpty()) return false;

        // Створюємо тимчасовий список для нових елементів
        Node tempHead = null;
        Node tempTail = null;
        int tempSize = 0;

        for (Byte b : c) {
            if (b == null) throw new NullPointerException();
            if (b < 0 || b >= base) throw new IllegalArgumentException("Digit must be between 0 and " + (base-1));

            Node newNode = new Node(b);
            if (tempHead == null) {
                tempHead = newNode;
                tempTail = newNode;
            } else {
                tempTail.next = newNode;
                newNode.prev = tempTail;
                tempTail = newNode;
            }
            tempSize++;
        }

        if (index == 0) {
            // Вставляємо на початок
            if (head != null) {
                tempTail.next = head;
                head.prev = tempTail;
                head = tempHead;
            } else {
                head = tempHead;
                tail = tempTail;
            }
        } else if (index == size) {
            // Вставляємо в кінець
            if (tail != null) {
                tail.next = tempHead;
                tempHead.prev = tail;
                tail = tempTail;
            } else {
                head = tempHead;
                tail = tempTail;
            }
        } else {
            // Вставляємо в середину
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            tempTail.next = current.next;
            if (current.next != null) {
                current.next.prev = tempTail;
            }
            current.next = tempHead;
            tempHead.prev = current;

            if (index == size) {
                tail = tempTail;
            }
        }

        size += tempSize;
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object obj : c) {
            while (remove(obj)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Node current = head;
        while (current != null) {
            Node next = current.next;
            if (!c.contains(current.data)) {
                remove(current.data);
                modified = true;
            }
            current = next;
        }
        return modified;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    @Override
    public Byte get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public Byte set(int index, Byte element) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
        if (element == null) throw new NullPointerException();
        if (element < 0 || element >= base) throw new IllegalArgumentException("Digit must be between 0 and " + (base-1));

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        byte oldValue = current.data;
        current.data = element;
        return oldValue;
    }

    @Override
    public void add(int index, Byte element) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();
        if (element == null) throw new NullPointerException();
        if (element < 0 || element >= base) throw new IllegalArgumentException("Digit must be between 0 and " + (base-1));

        Node newNode = new Node(element);

        if (index == 0) {
            // Вставка на початок
            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            }
        } else if (index == size) {
            // Вставка в кінець
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        } else {
            // Вставка в середину
            Node current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }

            newNode.next = current.next;
            newNode.prev = current;
            current.next.prev = newNode;
            current.next = newNode;
        }

        size++;
    }

    @Override
    public Byte remove(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException();

        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }

        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }

        size--;
        return current.data;
    }

    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Byte)) return -1;

        byte value = (Byte) o;
        Node current = head;
        int index = 0;

        while (current != null) {
            if (current.data == value) return index;
            current = current.next;
            index++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (!(o instanceof Byte)) return -1;

        byte value = (Byte) o;
        Node current = tail;
        int index = size - 1;

        while (current != null) {
            if (current.data == value) return index;
            current = current.prev;
            index--;
        }

        return -1;
    }

    @Override
    public ListIterator<Byte> listIterator() {
        return new ListIterator<Byte>() {
            private Node current = head;
            private Node lastReturned = null;
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Byte next() {
                if (!hasNext()) throw new NoSuchElementException();
                lastReturned = current;
                byte value = current.data;
                current = current.next;
                currentIndex++;
                return value;
            }

            @Override
            public boolean hasPrevious() {
                return current != head && head != null;
            }

            @Override
            public Byte previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                if (current == null) {
                    current = tail;
                } else {
                    current = current.prev;
                }
                lastReturned = current;
                currentIndex--;
                return current.data;
            }

            @Override
            public int nextIndex() {
                return currentIndex;
            }

            @Override
            public int previousIndex() {
                return currentIndex - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(Byte e) {
                if (lastReturned == null) throw new IllegalStateException();
                if (e == null) throw new NullPointerException();
                if (e < 0 || e >= base) throw new IllegalArgumentException("Digit must be between 0 and " + (base-1));

                lastReturned.data = e;
            }

            @Override
            public void add(Byte e) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public ListIterator<Byte> listIterator(int index) {
        if (index < 0 || index > size) throw new IndexOutOfBoundsException();

        ListIterator<Byte> iterator = listIterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator;
    }

    @Override
    public List<Byte> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException();
        }

        NumberListImpl sublist = new NumberListImpl();
        Node current = head;
        for (int i = 0; i < fromIndex; i++) {
            current = current.next;
        }

        for (int i = fromIndex; i < toIndex; i++) {
            sublist.add(current.data);
            current = current.next;
        }

        return sublist;
    }

    @Override
    public boolean swap(int index1, int index2) {
        if (index1 < 0 || index1 >= size || index2 < 0 || index2 >= size) {
            return false;
        }

        if (index1 == index2) return true;

        // Знаходимо перший вузол
        Node node1 = head;
        for (int i = 0; i < index1; i++) {
            node1 = node1.next;
        }

        // Знаходимо другий вузол
        Node node2 = head;
        for (int i = 0; i < index2; i++) {
            node2 = node2.next;
        }

        // Міняємо дані місцями
        byte temp = node1.data;
        node1.data = node2.data;
        node2.data = temp;

        return true;
    }

    @Override
    public void sortAscending() {
        // Проста сортування бульбашкою
        for (int i = 0; i < size - 1; i++) {
            Node current = head;
            for (int j = 0; j < size - i - 1; j++) {
                if (current.data > current.next.data) {
                    byte temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                }
                current = current.next;
            }
        }
    }

    @Override
    public void sortDescending() {
        // Проста сортування бульбашкою у зворотному порядку
        for (int i = 0; i < size - 1; i++) {
            Node current = head;
            for (int j = 0; j < size - i - 1; j++) {
                if (current.data < current.next.data) {
                    byte temp = current.data;
                    current.data = current.next.data;
                    current.next.data = temp;
                }
                current = current.next;
            }
        }
    }

    @Override
    public void shiftLeft() {
        if (size <= 1) return;

        Node first = head;
        head = head.next;
        head.prev = null;

        tail.next = first;
        first.prev = tail;
        first.next = null;
        tail = first;
    }

    @Override
    public void shiftRight() {
        if (size <= 1) return;

        Node last = tail;
        tail = tail.prev;
        tail.next = null;

        last.next = head;
        head.prev = last;
        last.prev = null;
        head = last;
    }
}
