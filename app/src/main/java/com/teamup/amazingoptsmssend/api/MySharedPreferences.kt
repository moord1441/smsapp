package com.teamup.amazingoptsmssend.api

import android.content.Context
import android.content.SharedPreferences



class SharedPreferencesHelper(context: Context, private val preferencesName: String = "MyPreferences") {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    fun saveList(key: String, list: List<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet(key, list.toSet())
        editor.apply()
    }
    fun getList(key: String): List<String> {
        val stringSet = sharedPreferences.getStringSet(key, emptySet())
        return stringSet?.toList() ?: emptyList()
    }
    fun containsValue(key: String, value: String): Boolean {
        val list = getList(key)
        return list.contains(value)
    }
    fun addToList(key: String, value: String) {
        val currentList = getList(key).toMutableSet()
        currentList.add(value)
        saveList(key, currentList.toList())
    }
    fun removeFromList(key: String, value: String) {
        val currentList = getList(key).toMutableList()
        currentList.remove(value)
        saveList(key, currentList)
    }
}

class BooleanSharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("BooleanSharedPreferences", Context.MODE_PRIVATE)


    fun saveBoolean(key  :String,value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key  :String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun removeBoolean(key  :String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }
}
class MyShared {
    private val sharedPreferences: SharedPreferences
    private val key: String

    constructor(context: Context, key: String) {
        sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        this.key = key
    }

    fun getValueList(): MutableList<String> {
        val savedList = sharedPreferences.getStringSet(key, mutableSetOf())?.toMutableList()
        return savedList ?: mutableListOf()
    }

    fun addValue(value: String) {
        val valueList = getValueList()
        valueList.add(value)
        sharedPreferences.edit().putStringSet(key, valueList.toMutableSet()).apply()
    }

    fun removeValue(value: String) {
        val valueList = getValueList()
        valueList.remove(value)
        sharedPreferences.edit().putStringSet(key, valueList.toMutableSet()).apply()
    }

    fun clearAllValues() {
        sharedPreferences.edit().remove(key).apply()
    }
}

class MySharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)

    fun saveList(key: String, list: List<String>) {
        val editor = sharedPreferences.edit()
        editor.putInt("${key}_size", list.size)
        list.forEachIndexed { index, value ->
            editor.putString("${key}_${index + 1}", value)
        }
        editor.apply()
    }

    fun getList(key: String): List<String> {
        val size = sharedPreferences.getInt("${key}_size", 0)
        val list = mutableListOf<String>()
        for (i in 1..size) {
            sharedPreferences.getString("${key}_$i", null)?.let { value ->
                list.add(value)
            }
        }
        return list
    }

    fun addToList(key: String, value: String) {
        val list = getList(key).toMutableList()
        list.add(value)
        saveList(key, list)
    }

    fun removeFromList(key: String, value: String) {
        val list = getList(key).toMutableList()
        list.remove(value)
        saveList(key, list)
    }
    fun containsValue(key: String, value: String): Boolean {
        val list = getList(key)
        return list.contains(value)
    }
}
