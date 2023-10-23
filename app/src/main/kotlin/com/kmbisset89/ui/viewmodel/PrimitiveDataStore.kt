package com.kmbisset89.ui.viewmodel

import kotlinx.serialization.Serializable

/**
 * A data store designed for efficiently storing and retrieving primitive data types.
 *
 * This class is intended to provide a clear and type-safe way to manage primitive data
 * without relying on Android-specific concepts like Bundles. It provides a clear API
 * for storing and retrieving data by using individual maps for each primitive type.
 *
 * @property stringMap Storage for string values.
 * @property intMap Storage for integer values.
 * @property longMap Storage for long values.
 * @property floatMap Storage for float values.
 * @property doubleMap Storage for double values.
 * @property booleanMap Storage for boolean values.
 * @property charMap Storage for char values.
 * @property byteMap Storage for byte values.
 * @property shortMap Storage for short values.
 */
@Serializable
data class PrimitiveDataStore(
    val stringMap: MutableMap<String, String> = mutableMapOf(),
    val intMap: MutableMap<String, Int> = mutableMapOf(),
    val longMap: MutableMap<String, Long> = mutableMapOf(),
    val floatMap: MutableMap<String, Float> = mutableMapOf(),
    val doubleMap: MutableMap<String, Double> = mutableMapOf(),
    val booleanMap: MutableMap<String, Boolean> = mutableMapOf(),
    val charMap: MutableMap<String, Char> = mutableMapOf(),
    val byteMap: MutableMap<String, Byte> = mutableMapOf(),
    val shortMap: MutableMap<String, Short> = mutableMapOf()
) {
    /**
     * Store a string value.
     * @param key The key to store the value.
     * @param value The string value to store.
     */
    fun putString(key: String, value: String) = stringMap.put(key, value)

    /**
     * Retrieve a string value.
     * @param key The key to retrieve the value.
     * @return The retrieved string value or null if not present.
     */
    fun getString(key: String): String? = stringMap[key]

    /**
     * Store an integer value.
     * @param key The key to store the value.
     * @param value The integer value to store.
     */
    fun putInt(key: String, value: Int) = intMap.put(key, value)

    /**
     * Retrieve an integer value.
     * @param key The key to retrieve the value.
     * @return The retrieved integer value or null if not present.
     */
    fun getInt(key: String): Int? = intMap[key]

    /**
     * Store a long value.
     * @param key The key to store the value.
     * @param value The long value to store.
     */
    fun putLong(key: String, value: Long) = longMap.put(key, value)

    /**
     * Retrieve a long value.
     * @param key The key to retrieve the value.
     * @return The retrieved long value or null if not present.
     */
    fun getLong(key: String): Long? = longMap[key]

    /**
     * Store a float value.
     * @param key The key to store the value.
     * @param value The float value to store.
     */
    fun putFloat(key: String, value: Float) = floatMap.put(key, value)

    /**
     * Retrieve a float value.
     * @param key The key to retrieve the value.
     * @return The retrieved float value or null if not present.
     */
    fun getFloat(key: String): Float? = floatMap[key]

    /**
     * Store a double value.
     * @param key The key to store the value.
     * @param value The double value to store.
     */
    fun putDouble(key: String, value: Double) = doubleMap.put(key, value)

    /**
     * Retrieve a double value.
     * @param key The key to retrieve the value.
     * @return The retrieved double value or null if not present.
     */
    fun getDouble(key: String): Double? = doubleMap[key]

    /**
     * Store a boolean value.
     * @param key The key to store the value.
     * @param value The boolean value to store.
     */
    fun putBoolean(key: String, value: Boolean) = booleanMap.put(key, value)

    /**
     * Retrieve a boolean value.
     * @param key The key to retrieve the value.
     * @return The retrieved boolean value or null if not present.
     */
    fun getBoolean(key: String): Boolean? = booleanMap[key]

    /**
     * Store a char value.
     * @param key The key to store the value.
     * @param value The char value to store.
     */
    fun putChar(key: String, value: Char) = charMap.put(key, value)

    /**
     * Retrieve a char value.
     * @param key The key to retrieve the value.
     * @return The retrieved char value or null if not present.
     */
    fun getChar(key: String): Char? = charMap[key]

    /**
     * Store a byte value.
     * @param key The key to store the value.
     * @param value The byte value to store.
     */
    fun putByte(key: String, value: Byte) = byteMap.put(key, value)

    /**
     * Retrieve a byte value.
     * @param key The key to retrieve the value.
     * @return The retrieved byte value or null if not present.
     */
    fun getByte(key: String): Byte? = byteMap[key]

    /**
     * Store a short value.
     * @param key The key to store the value.
     * @param value The short value to store.
     */

    fun putShort(key: String, value: Short) = shortMap.put(key, value)

    /**
     * Retrieve a short value.
     * @param key The key to retrieve the value.
     * @return The retrieved short value or null if not present.
     */
    fun getShort(key: String): Short? = shortMap[key]
}