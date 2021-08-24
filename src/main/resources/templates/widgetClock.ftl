		<#if time1Name?has_content && time1Time?has_content && time2Name?has_content && time2Time?has_content>
		<div id="dual-clock" class="bottom-left timestamp timestamp-grid">
			<p class="label col1to3">Last Refresh:</p>
			<p class="label">${time1Name}</p><p>${time1Time}</p>
			<p class="label">${time2Name}</p><p>${time2Time}</p>
		</div>
		<script>
			const theClock = document.getElementById('dual-clock');
			dragElement(theClock);
		</script>
		</#if>
