/*package com.loserstar.utils.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import jodd.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GsonUtils {

	*//**
	 * 是否格式化json打印
	 *//*
	public static boolean IS_PRETTY_PRINT = true;
	
	*//**
	 * 根据cls和泛型类型反序列化json字符串
	 *
	 * @param json
	 * @param cls
	 * @param genericCls
	 * @return
	 *//*
	public static <T extends Object> T toObject(String json, Class<T> cls, Type... genericCls) {
		ParameterizedType pt = new ParameterizedType() {
			@Override
			public Type[] getActualTypeArguments() {
				return genericCls;
			}

			@Override
			public Type getRawType() {
				return cls;
			}

			@Override
			public Type getOwnerType() {
				return null;
			}
		};
		return getGsonBuilder().create().fromJson(json, pt);
	}

	*//**
	 * 根据cls反序列化json字符串
	 *
	 * @param json
	 * @param cls
	 * @return
	 *//*
	public static <T extends Object> T fromJson(String json, Class<T> cls) {
		return (T) getGsonBuilder().create().fromJson(json, cls);
	}

	public static <T extends Object> T fromJson(File f, Class<T> cls) throws IOException {
		return fromJson(FileUtil.readString(f, "utf-8"), cls);
	}

	public static JsonObjGetter fromJson(File f) throws IOException {
		return fromJson(FileUtil.readString(f, "utf-8"));
	}

	public static JsonObjGetter fromJson(String json) {
		return new JsonObjGetter(getGsonBuilder().create().fromJson(json, Object.class));
	}

	public static String toJson(Object obj) {
		return getGsonBuilder().create().toJson(obj);
	}

	public static String toJson(Object obj, boolean prettyPrinting) {
		return getGsonBuilder(prettyPrinting).create().toJson(obj);
	}

	public static void writeJson(String file, Object obj) throws IOException {
		FileUtil.writeString(file, getGsonBuilder().create().toJson(obj), "utf-8");
	}

	*//**
	 * 构建通用GsonBuilder, 封装初始化工作
	 *
	 * @return
	 *//*
	public static GsonBuilder getGsonBuilder() {
		return getGsonBuilder(IS_PRETTY_PRINT);
	}

	*//**
	 * 构建通用GsonBuilder, 封装初始化工作
	 *
	 * @return
	 *//*
	public static GsonBuilder getGsonBuilder(boolean prettyPrinting) {
		GsonBuilder gb = new GsonBuilder();
		gb.setDateFormat("yyyy-MM-dd HH:mm:ss:mss");
		gb.setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				return f.getAnnotation(WJsonExclued.class) != null;
			}

			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return clazz.getAnnotation(WJsonExclued.class) != null;
			}
		});
		if (prettyPrinting)
			gb.setPrettyPrinting();
		return gb;
	}
	public static String getStr(JsonObjGetter getter,String key){
		return GsonUtils.getStr(getter, key, "");
	}
	
	public static String getStr(JsonObjGetter getter,String key,String defaultVal){
		return getter.str(key) != null ? getter.str(key):defaultVal;
	}
	
	public static int getInt(JsonObjGetter getter,String key){
		return GsonUtils.getInt(getter, key, -1);
	}
	
	public static int getInt(JsonObjGetter getter,String key,int defaultVal){
		return getter.str(key) != null ? Double.valueOf(getter.str(key)).intValue():defaultVal;
	}
}
*/