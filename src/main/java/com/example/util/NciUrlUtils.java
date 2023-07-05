package com.example.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理非车前端页面链接不规范的问题
 * <p>
 * 一个合法的uri格式应该如下：
 * <pre>
 *   foo://example.com:8042/over/there?name=ferret#nose
 *   \_/   \______________/\_________/ \_________/ \__/
 *    |           |            |            |        |
 *  scheme     authority     path        query   fragment
 *    |   _____________________|__
 *   / \ /                        \
 *   urn:example:animal:ferret:nose
 * </pre>
 * 但是非车这边仍存在部分不符合规范的URL格式，如：
 * <pre>
 * https://hcz-static.pingan.com.cn/activities/otherInsurance/index.html<font color="red">#</font>/insureIndex<font color="orange">?</font>productId=20180607606388
 * </pre>
 * 上面这条链接在fragment参数后面再试图拼接query参数，正确的链接拼接方式应该如下，即query应该在path和fragment之间：
 * <pre>
 * https://hcz-static.pingan.com.cn/activities/otherInsurance/index.html<font color="orange">?</font>productId=20180607606388<font color="red">#</font>/insureIndex
 * </pre>
 * 这类不规范的链接会导致无法使用通用的工具类进行拼接参数，只能手动拼接字符串，导致容易出现一些不可预见的错误。
 * 这个工具类主要用于规范化这部分链接，并提供一些query参数拼接及获取的工具方法。
 *
 * @author RUJIAHAO798
 * @see URI
 * @see <a href="https://www.ietf.org/rfc/rfc3986.txt">
 * <i>RFC&nbsp;3986: Uniform Resource Identifier (URI): Generic Syntax</i></a>
 */
@Slf4j
public class NciUrlUtils {

    private static final String QUERY_REGEX = "(?<=\\?)[^?#]+";
    private static final Pattern QUERY_PATTERN = Pattern.compile(QUERY_REGEX);
    private static final String FRAGMENT_REGEX = "(?<=#)[^?#]+";
    private static final Pattern FRAGMENT_PATTERN = Pattern.compile(FRAGMENT_REGEX);

    private NciUrlUtils() {/* cannot instantiate */}

    /**
     * 标准化非车前端页面链接
     *
     * @param url 原始url
     * @return 处理后的url
     */
    public static String standardize(@NonNull String url) {
        // 去除链接前后的空字符
        String trimmed = StringUtils.trim(url);
        // 匹配url中存在的query
        Matcher queryMatcher = QUERY_PATTERN.matcher(trimmed);
        List<String> matchedQueryParts = new ArrayList<>();
        while (queryMatcher.find()) {
            String group = queryMatcher.group();
            matchedQueryParts.add(group);
        }
        // 匹配url中的fragment
        Matcher fragmentMatcher = FRAGMENT_PATTERN.matcher(trimmed);
        List<String> matchedFragmentParts = new ArrayList<>();
        while (fragmentMatcher.find()) {
            String group = fragmentMatcher.group();
            matchedFragmentParts.add(group);
        }
        // 获取链接除 query 和 fragment 以外的部分
        String[] split = trimmed.split("[?#]");
        if (split.length == 0) {
            return trimmed;
        }
        String base = split[0];
        // 构建最终的链接
        StringBuilder sb = new StringBuilder()
                .append(base);
        if (!matchedQueryParts.isEmpty()) {
            sb.append("?");
            // 多个 query 的情况下，将多个参数合并
            for (String matchedQueryPart : matchedQueryParts) {
                sb.append(matchedQueryPart).append("&");
            }
            sb.setLength(sb.length() - 1);
        }
        if (!matchedFragmentParts.isEmpty()) {
            // 存在多个 fragment 的情况下，仅使用最后的 fragment
            String fragment = matchedFragmentParts.get(matchedFragmentParts.size() - 1);
            sb.append("#").append(fragment);
        }
        return sb.toString();
    }

    /**
     * 根据传入的 url 标准化以后，返回对应的 {@link UriComponentsBuilder} 用于拼接参数
     * <p>
     * 用例：
     * <pre>
     * UriComponents example = NciUrlUtils
     *         .builder("https://example.com/index.html#/header?invalidKey=value")
     *         .queryParam("key", "value")
     *         .build();
     * // https://example.com/index.html?invalidKey=value&key=value#/header
     * System.out.println(example.toString());
     * </pre>
     * 使用这种方式进行参数拼接可以保证参数被拼接到正确的位置。
     *
     * @param url 原始url
     * @return 标准化后的原始链接对应的 {@link UriComponentsBuilder}
     * @see UriComponentsBuilder
     * @see UriComponents
     */
    public static UriComponentsBuilder builder(String url) {
        return UriComponentsBuilder.fromUriString(standardize(url));
    }

    /**
     * 根据传入的 url 标准化以后，返回对应的 {@link UriComponents} 用于获取查询参数
     *
     * @param url 原始url
     * @return 标准化后的原始链接对应的 {@link UriComponents}
     * @see UriComponentsBuilder
     * @see UriComponents
     */
    public static UriComponents parse(String url) {
        return builder(url).build();
    }

    /**
     * 根据传入的 url 标准化以后，返回对应的 {@link URI}
     *
     * @param url 原始url
     * @return 标准化后的原始链接对应的 {@link URI}
     * @see URI
     */
    public static URI toUri(String url) {
        return parse(url).toUri();
    }
    /**
     * 是否包含fragment参数
     * @param url
     * @return
     */
    public static boolean isContainFragment(String url){
        Matcher matcher = FRAGMENT_PATTERN.matcher(url);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 是否包含Query参数
     * @param url
     * @return
     */
    public static boolean isContainQuery(String url){
        Matcher queryMatcher = QUERY_PATTERN.matcher(url);
        if (queryMatcher.find()) {
            return true;
        }
        return false;
    }
}
