<?xml version="1.0" encoding="utf-8"?>
<%@ page contentType="text/xml;charset=utf-8" %>
<%@ taglib uri="c.tld" prefix="c" %>
<!-- added xml tags for KEK and AUK keys -->
<key_data>
   <key_rec>		
		<key_kek><c:out value="${requestScope.keyKEK}"/></key_kek>
		<key_auk><c:out value="${requestScope.keyAUK}"/></key_auk>
		<key_kek_part1><c:out value="${requestScope.keyKEKPart1}"/></key_kek_part1>
		<key_kek_part2><c:out value="${requestScope.keyKEKPart2}"/></key_kek_part2>
		<key_kvv><c:out value="${requestScope.keyKVV}"/></key_kvv>
		<generation_no><c:out value="${requestScope.generationNumber}"/></generation_no>
		<generation_date><c:out value="${requestScope.generationDateTime}"/></generation_date>
		<customer_name1><c:out value="${requestScope.customerName1}"/></customer_name1>
		<customer_name2><c:out value="${requestScope.customerName2}"/></customer_name2>
		<control_code><c:out value="${requestScope.controlCode}"/></control_code>

		<KEK_GENERATION_NO><c:out value="${requestScope.KEK_GENERATION_NO}"/></KEK_GENERATION_NO>
		<GENERATION_DATE><c:out value="${requestScope.GENERATION_DATE}"/></GENERATION_DATE>
		<KVV><c:out value="${requestScope.KVV}"/></KVV>
		<AUK_GENERATION_NO><c:out value="${requestScope.AUK_GENERATION_NO}"/></AUK_GENERATION_NO>
		<KEK_GENERATION_NO1><c:out value="${requestScope.KEK_GENERATION_NO1}"/></KEK_GENERATION_NO1>
		<GENERATION_DATE1><c:out value="${requestScope.GENERATION_DATE1}"/></GENERATION_DATE1>
		<KVV1><c:out value="${requestScope.KVV1}"/></KVV1>
		<KEY_STATUS><c:out value="${requestScope.KEY_STATUS}"/></KEY_STATUS>

		<customer_address1><c:out value="${requestScope.address1}"/></customer_address1>
		<customer_address2><c:out value="${requestScope.address2}"/></customer_address2>
	</key_rec>		
</key_data>