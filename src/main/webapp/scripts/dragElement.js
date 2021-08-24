
const widgets = [];

window.onresize = _ => {
	if (document.readyState !== "complete") return;
	let newTop;
	let newLeft
	for (const widget of widgets) {
		if (window.innerWidth < widget.offsetWidth + 20 || window.innerHeight < widget.offsetHeight + 10) continue;
		newTop = widget.offsetTop;
		newLeft = widget.offsetLeft;
		if (newTop > window.innerHeight - widget.offsetHeight - 5) newTop = window.innerHeight - widget.offsetHeight - 5;
		if (newLeft > window.innerWidth - widget.offsetWidth - 15) newLeft = window.innerWidth - widget.offsetWidth - 15;
		widget.style.top = newTop + 'px';
		widget.style.left = newLeft + 'px';
	}
};

const dragElement = (clickedElement, draggedElement) => {
	if (!clickedElement) return;
	if (!draggedElement) draggedElement = clickedElement;
	widgets.push(draggedElement);

	// Make sure object is bound to "top" and "left" instead of "bottom" and "right":
	draggedElement.style.top = draggedElement.offsetTop + 'px';
	draggedElement.style.left = draggedElement.offsetLeft + 'px';
	draggedElement.style.right = 'auto';
	draggedElement.style.bottom = 'auto';

	let pos1 = 0;
	let pos2 = 0;
	let pos3 = 0;
	let pos4 = 0;

	const dragMouseDown = (e) => {
		e = e || window.event;
		e.preventDefault();
		// get the mouse cursor position at startup:
		pos3 = e.clientX;
		pos4 = e.clientY;
		document.onmouseup = closeDragElement;
		// call a function whenever the cursor moves:
		document.onmousemove = elementDrag;
	};

	const elementDrag = (e) => {
		e = e || window.event;
		e.preventDefault();
		// calculate the new cursor position:
		pos1 = pos3 - e.clientX;
		pos2 = pos4 - e.clientY;
		pos3 = e.clientX;
		pos4 = e.clientY;
		
		// Make sure element is within bounds
		// Note: Due to the scrollbar, the horizontal needs to be an extra 20 pixels in -- or at least somewhat
		let newTop = draggedElement.offsetTop - pos2;
		if (newTop < 3) newTop = 3;
		if (newTop > window.innerHeight - draggedElement.offsetHeight - 5) newTop = window.innerHeight - draggedElement.offsetHeight - 5;

		let newLeft = draggedElement.offsetLeft - pos1;
		if (newLeft < 3) newLeft = 3;
		if (newLeft > window.innerWidth - draggedElement.offsetWidth - 15) newLeft = window.innerWidth - draggedElement.offsetWidth - 15;

		// Set new position:
		draggedElement.style.top = newTop + 'px';
		draggedElement.style.left = newLeft + 'px';
	};

	const closeDragElement = _ => {
		// stop moving when mouse button is released:
		document.onmouseup = null;
		document.onmousemove = null;
	};

	clickedElement.onmousedown = dragMouseDown;

};
