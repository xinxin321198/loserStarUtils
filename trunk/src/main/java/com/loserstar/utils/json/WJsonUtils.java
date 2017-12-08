package com.loserstar.utils.json;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;

import jodd.io.FileUtil;

public class WJsonUtils {

	public static JsonObjGetter fromJson(File f) {
		try {
			return fromJson(FileUtil.readString(f, "utf-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends Object> T fromJson(File f, Class<T> cls) {
		try {
			return fromJson(FileUtil.readString(f, "utf-8"), cls);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JsonObjGetter fromJson(String json) {
		try {
			return new JsonObjGetter(getGsonBuilder(true).create().fromJson(json, Object.class));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据cls反序列化json字符串
	 *
	 * @param json
	 * @param cls
	 * @return
	 */
	public static <T extends Object> T fromJson(String json, Class<T> cls) {
		return getGsonBuilder(true).create().fromJson(json, cls);
	}


	/**
	 * 构建通用GsonBuilder, 封装初始化工作
	 *
	 * @return
	 */
	public static GsonBuilder getGsonBuilder(boolean prettyPrinting) {
		GsonBuilder gb = new GsonBuilder();
		gb.setDateFormat("yyyy-MM-dd HH:mm:ss:mss");
		gb.setExclusionStrategies(new ExclusionStrategy() {
			@Override
			public boolean shouldSkipClass(Class<?> clazz) {
				return clazz.getAnnotation(WJsonExclued.class) != null || clazz.getAnnotation(JsonIgnore.class) != null;
			}

			@Override
			public boolean shouldSkipField(FieldAttributes f) {
				return f.getAnnotation(WJsonExclued.class) != null || f.getAnnotation(JsonIgnore.class) != null;
			}
		});
		if (prettyPrinting) {
			gb.setPrettyPrinting();
		}
		return gb;
	}

	public static int getInt(JsonObjGetter getter, String key) {
		return WJsonUtils.getInt(getter, key, -1);
	}

	public static int getInt(JsonObjGetter getter, String key, int defaultVal) {
		return getter.str(key) != null ? Double.valueOf(getter.str(key)).intValue() : defaultVal;
	}

	public static String getStr(JsonObjGetter getter, String key) {
		return WJsonUtils.getStr(getter, key, "");
	}

	public static String getStr(JsonObjGetter getter, String key, String defaultVal) {
		return getter.str(key) != null ? getter.str(key) : defaultVal;
	}

	public static String toJson(Object obj) {
		return getGsonBuilder(true).create().toJson(obj);
	}

	public static String toJson(Object obj, boolean prettyPrinting) {
		return getGsonBuilder(prettyPrinting).create().toJson(obj);
	}

	public static Object toObject(String json) {
		try {
			return fromJson(json, Object.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据cls和泛型类型反序列化json字符串
	 *
	 * @param json
	 * @param cls
	 * @param genericCls
	 * @return
	 */
	public static <T extends Object> T toObject(String json, Class<T> cls, Type... genericCls) {
		ParameterizedType pt = new ParameterizedType() {
			@Override
			public Type[] getActualTypeArguments() {
				return genericCls;
			}

			@Override
			public Type getOwnerType() {
				return null;
			}

			@Override
			public Type getRawType() {
				return cls;
			}
		};
		return getGsonBuilder(true).create().fromJson(json, pt);
	}

	public static void writeJson(String file, Object obj) {
		try {
			FileUtil.writeString(file, getGsonBuilder(true).create().toJson(obj), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T extends Object> T reGetObj(T obj, Class<T> cls, Type... genericCls) {
		String json = WJsonUtils.toJson(obj);
		return toObject(json, cls, genericCls);
	}
}
