/* ---------------------------------------------------------------
* Left menu management
*  --------------------------------------------------------------- */
document.addEventListener("DOMContentLoaded", function () {
    // Add 'active' class on click
    const navLinks = document.querySelectorAll('.nav-link');

    navLinks.forEach(link => {
        link.addEventListener('click', function () {
            navLinks.forEach(item => item.classList.remove('active'));
            this.classList.add('active');
        });
    });

    //
    document.getElementById("collapseBtn").addEventListener("click", function () {
        let mainContent = document.getElementById('mainContent');
        let sidebar = document.getElementById("sidebar");
        let submenus = document.querySelectorAll(".leftSubmenu");
        let arrows = document.querySelectorAll(".dropdown-icon");
        // Toggle the collapsed class on the mainContent and sidebar
        mainContent.classList.toggle('collapsed');
        sidebar.classList.toggle("collapsed");
        // When collapsing, close all submenus and reset the arrows
        if (sidebar.classList.contains("collapsed")) {
            submenus.forEach(menu => {
                menu.classList.remove("show");
            });
            arrows.forEach(arrow => {
                arrow.style.transform = 'rotate(0deg)';  // Reset arrows to default state
            });
        } else {
            // Reset any open submenus when expanding the sidebar
            submenus.forEach(menu => {
                if (menu.classList.contains("show")) {
                    menu.classList.remove("show");
                }
            });
        }
    });

// Adjust arrow direction on menu toggle
    document.querySelectorAll("[data-bs-toggle='collapse']").forEach(item => {
        item.addEventListener('click', function () {
            let icon = this.querySelector('.dropdown-icon');
            if (icon) {
                const submenu = this.nextElementSibling;
                if (submenu.classList.contains("show")) {
                    icon.style.transform = "rotate(90deg)";  // Point right when expanded
                } else {
                    icon.style.transform = "rotate(0deg)";  // Point down when collapsed
                }
            }
        });
    });

// Listen for the collapse events to adjust icon after transition
    document.querySelectorAll('.collapse').forEach(collapseElement => {
        collapseElement.addEventListener('shown.bs.collapse', function () {
            let icon = this.previousElementSibling.querySelector('.dropdown-icon');
            if (icon) {
                icon.style.transform = "rotate(90deg)";  // Point right when expanded
            }
        });

        collapseElement.addEventListener('hidden.bs.collapse', function () {
            let icon = this.previousElementSibling.querySelector('.dropdown-icon');
            if (icon) {
                icon.style.transform = "rotate(0deg)";  // Point down when collapsed
            }
        });
    });
});

/** ---------------------------------------------------------------------------------------------
 * Top menu button management: List, add display, edit, delete
 --------------------------------------------------------------------------------------------- */
// buttons
function modeListBtn() {
    document.getElementById('topmenuList').style.display = 'none';
    document.getElementById('topmenuAdd').style.display = 'inline-block';
    document.getElementById("topmenuDisplay").style.display = 'none';
    document.getElementById("topmenuEdit").style.display = 'none';
    document.getElementById("topmenuDelete").style.display = 'none';
}

function modeDisplayBtn() {
    document.getElementById("topmenuList").style.display = 'inline-block';
    document.getElementById("topmenuAdd").style.display = 'inline-block';
    document.getElementById("topmenuDisplay").style.display = 'none';
    document.getElementById("topmenuEdit").style.display = 'inline-block';
    document.getElementById("topmenuDelete").style.display = 'inline-block';
}

function modeEditBtn() {
    document.getElementById("topmenuList").style.display = 'inline-block';
    document.getElementById("topmenuAdd").style.display = 'none';
    document.getElementById("topmenuDisplay").style.display = 'inline-block';
    document.getElementById("topmenuEdit").style.display = 'none';
    document.getElementById("topmenuDelete").style.display = 'none';
}

function topMenuDisplay() {
    let currentUrl = window.location.pathname;
    let baseUrl = currentUrl.split('/')[1];
    // list and add case, if not home url
    if (baseUrl.length === 0 || baseUrl === 'dashboard') {
        return;
    }
    document.getElementById("topmenuList").setAttribute("href", `/${baseUrl}`);
    // document.getElementById("topmenuAdd").setAttribute("href", `/${baseUrl}/add`);

    //
    let currentPathParts = currentUrl.split('/');
    if (currentPathParts.length === 2) {
        modeListBtn();
    } else if (currentPathParts.length >= 3) {
        let section = currentPathParts[2];
        let queryPart = window.location.search;

        const urlParams = new URLSearchParams(queryPart);
        const idParam = urlParams.get("id");

        if (section === "display" || section === "edit") {
            document.getElementById("topmenuDisplay").setAttribute("href", `/${baseUrl}/display` + queryPart);
            document.getElementById("topmenuEdit").setAttribute("href", `/${baseUrl}/edit` + queryPart);
            document.getElementById("topmenuDelete").setAttribute("data-id", idParam);
        }
        //
        if (currentUrl.includes('display')) {
            modeDisplayBtn();
        } else if (currentUrl.includes('edit')) {
            modeEditBtn();
        } else {
            modeListBtn();
        }
    }

    // Special cases: hide Add button
    let excludeBtnAdd = ["user", "admin"];
    if (excludeBtnAdd.includes(baseUrl)) { // user
        document.getElementById('topmenuList').style.display = 'none';
        document.getElementById('topmenuAdd').style.display = 'none';
        document.getElementById('topmenuDelete').style.display = 'none';
    }


    /*if (baseUrl === "user") {
        document.getElementById('topmenuAdd').style.display = 'none';
        document.getElementById('topmenuList').style.display = 'none';
    }*/
    //
    displaySessionstorageToast(); // display message if any in session storage

}

// click add (top menu button)
document.addEventListener("DOMContentLoaded", function () {
    let btnAdd = document.getElementById("topmenuAdd");
    btnAdd.addEventListener("click", function (e) {
        e.preventDefault();

        let actualUrl = window.location.pathname;
        let partQueries = window.location.search
        let baseRoute = actualUrl.split('/')[1];
        const urlParamList = new URLSearchParams(partQueries);

        //
        fetch(`/${baseRoute}/add`, {
            method: "POST",
            headers: {
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.messageType) {
                    let toastMessage = {
                        "messageType": data.messageType,
                        "messageHeader": data.messageHeader,
                        "messageBody": data.messageBody
                    };
                    if (data.messageType === "success"
                        && typeof data.detailList === "object"
                        && Object.keys(data.detailList).length > 0
                        && data.detailList.id) {
                        sessionStorage.setItem("toastMessage", JSON.stringify(toastMessage));
                        window.location.href = `/${baseRoute}/edit?id=`+data.detailList.id;
                    } else {
                        showToast(toastMessage);
                    }
                } else {
                    showToast({
                        "messageType": "error",
                        "messageHeader": "Something went wrong",
                        "messageBody": "An error occurred when adding new " + baseRoute
                    });
                }
            })
            .catch(error => {
                showToast({
                    "messageType": "error",
                    "messageHeader": "Something went wrong",
                    "messageBody": "Unable to add new " + baseRoute
                });
                console.log(error)
            });

    });
});
/** ---------------------------------------------------------------------------------------------
 *  table row clickable
 --------------------------------------------------------------------------------------------- */
function listRowClickable() {
    document.querySelectorAll(".clickable-row").forEach(row => {
        row.addEventListener("click", function (event) {
            // Prevent redirection when clicking on buttons or links inside the row
            if (event.target.tagName === "A" || event.target.closest("a")) {
                return;
            }
            let idObject = this.getAttribute("data-id");
            let currentUrl = window.location.pathname;
            let baseUrl = currentUrl.split('/')[1];

            window.location.href = `/${baseUrl}/display?id=${idObject}`;
        });
    });
}

//
document.addEventListener("DOMContentLoaded", function () {
    topMenuDisplay();
    listRowClickable();
    // delete btn case
    let currentUrl = window.location.pathname;
    if (currentUrl.includes('display')) {
        let btnDeleteButton = document.getElementById("topmenuDelete");
        let btnConfirmButton = document.getElementById("btnModalOK");
        //
        btnDeleteButton.addEventListener("click", function (e) {
            e.preventDefault();
            showModal();
        });
        // Confirm modal action : perform delete if possible
        btnConfirmButton.addEventListener("click", function (e) {
            e.preventDefault();
            hideModal();
            let actualUrl = window.location.pathname;
            let partQueries = window.location.search
            let baseRoute = actualUrl.split('/')[1];
            const urlParamList = new URLSearchParams(partQueries);
            const currentId = urlParamList.get("id");

            fetch(`/${baseRoute}/delete?id=${currentId}`, {
                method: "DELETE",
                headers: {
                    'Accept': 'application/json'
                }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.messageType) {
                        let toastMessage = {
                            "messageType": data.messageType,
                            "messageHeader": data.messageHeader,
                            "messageBody": data.messageBody
                        };
                        if (data.messageType === "success") {
                            sessionStorage.setItem("toastMessage", JSON.stringify(toastMessage));
                            window.location.href = `/${baseRoute}`;
                        } else {
                            hideModal();
                            showToast(toastMessage);
                        }

                    } else {
                        hideModal();
                        showToast({
                            "messageType": "error",
                            "messageHeader": "Something went wrong",
                            "messageBody": "An error occurred when deleting."
                        });
                    }
                })
                .catch(error => {
                    hideModal();
                    showToast({
                        "messageType": "error",
                        "messageHeader": "Something went wrong",
                        "messageBody": "An error occurred when deleting."
                    });
                    console.log(error)
                }).finally(function () {
                hideModal();
            });
        });
        // End perform delete
    }
});

/** ---------------------------------------------------------------------------------------------
 * Toast management
 --------------------------------------------------------------------------------------------- */
function showToast(message) {
    if (message === null || typeof message !== "object" || Object.keys(message).length <= 0) {
        return;
    }
    if (!Object.hasOwn(message, "messageType")) {
        return;
    }
    let setup = messageTypeSetup(message.messageType);
    const toastContainer = document.getElementById("toastContainer");
    const toast = document.createElement("div");
    toast.className = `toast bg-${setup.classColor ?? "info"} bg-opacity-25 border-1 show`;
    toast.innerHTML = `
                        <div class="toast-header" id="toastHeader">
                            <strong class="me-auto" id="toastHeaderMessage">${message.messageHeader}</strong>
                            <small>Just now</small>
                            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                        </div>
                        <div class="toast-body" id="toastBody">${message.messageBody}<br/></div>`;
    toastContainer.appendChild(toast);
    // Automatically hide toast after 10 seconds
    setTimeout(() => {
        toast.remove();
    }, 10000);
}

function messageTypeSetup(messageType) {
    let setup = {};
    switch (messageType) {
        case "error":
            setup.classColor = "danger";
            break;
        case "success":
            setup.classColor = "success";
            break;
        default:
            setup.classColor = "info";
            break;
    }
    return setup;
}

function displaySessionstorageToast() {
    const storedMessage = sessionStorage.getItem("toastMessage");
    if (storedMessage) {
        try {
            let message = JSON.parse(storedMessage);
            showToast(message);
            console.dir(message);
            sessionStorage.removeItem("toastMessage");
        } catch (error) {
            console.error("Invalid JSON:", error.message);
        }
    }
}

/** ---------------------------------------------------------------------------------------------
 * Form management
 --------------------------------------------------------------------------------------------- */
// Check for Ctrl + S
document.addEventListener("keydown", function (event) {
    if ((event.ctrlKey || event.metaKey) && event.key === 's') {
        event.preventDefault();
        document.getElementById("btnSubmitEvtform").click();
    }
});

// edit form
document.addEventListener("DOMContentLoaded", function () {
    let form = document.getElementById("eventForm");
    if (form !== null) {
        form.addEventListener("submit", async function (event) {
            event.preventDefault();

            let isValid = true;

            //
            form.querySelectorAll("input, select, textarea").forEach(field => {
                field.classList.remove("border-danger");
            });
            //
            form.querySelectorAll("input, select, textarea").forEach(field => {
                if (!field.checkValidity()) {
                    field.classList.add("border-danger");
                    isValid = false;
                }
            });
            //
            if (!isValid) {
                var toastMessage = {
                    "messageType": "error",
                    "messageHeader": "Validation Failed",
                    "messageBody": "Please correct the highlighted errors."
                };
                showToast(toastMessage);
            }

            // Form is valid, proceed to submit
            if (isValid) {
                showButtonSpinner();
                //
                const formData = new FormData(form);
                /*for (let [key, value] of formData.entries()) {
                    console.log(`${key}:`, value);
                }*/
                //
                let actualUrl = window.location.pathname;
                let partQueries = window.location.search
                let baseRoute = actualUrl.split('/')[1];
                const urlParamList = new URLSearchParams(partQueries);
                const currentId = urlParamList.get("id");

                try {
                    const response = await fetch(`/${baseRoute}/edit`, {
                        method: "POST",
                        body: formData,
                        headers: {
                            'Accept': 'application/json'
                        }
                    });
                    //
                    const data = await response.json();

                    if (data.messageType) {
                        hideButtonSpinner();
                        var toastMessage = {
                            "messageType": data.messageType,
                            "messageHeader": data.messageHeader,
                            "messageBody": data.messageBody
                        };

                        if (data.messageType === "success") {
                            if (baseRoute === "user") {
                                window.location.href = `/auth/login?logout`;
                            } else {
                                sessionStorage.setItem("toastMessage", JSON.stringify(toastMessage));
                                window.location.href =  `/${baseRoute}/display?id=${currentId}`;
                            }
                        } else if (data.messageType === "error") {
                            if (data.detailList
                                && typeof data.detailList === "object"
                                && Object.keys(data.detailList).length > 0) {
                                // Loop through each key-value pair
                                let addMessage = "<ul>";
                                for (const [fieldName, errorMessage] of Object.entries(data.detailList)) {
                                    // console.log(fieldName, ":", errorMessage);
                                    const field = document.querySelector(`[name="${fieldName}"]`);
                                    if (field) {
                                        field.classList.add("border-danger");
                                        addMessage += errorMessage ? `<li>${errorMessage}</li>` : ``;
                                    }
                                }
                                toastMessage.messageBody += addMessage + "</ul>";
                                showToast(toastMessage);
                            } else {
                                showToast(toastMessage);
                            }
                        }

                    } else {
                        showToast({
                            "messageType": "error",
                            "messageHeader": "Something went wrong",
                            "messageBody": "An error occurred when updating."
                        });
                    }

                } catch (error) {
                    console.error("Error:", error);
                } finally {
                    hideButtonSpinner();
                }
            }
        });
    }
});

// Spinner for submit button on edit pages
function showButtonSpinner() {
    const btn = document.getElementById("btnSubmitEvtform");
    btn.disabled = true;
    btn.type = "button";
    btn.innerHTML = `
        <span id="spanSpinner" class="spinner-border spinner-border-sm" aria-hidden="true"></span>
        <span id="spanSpinnerStatus" role="status">saving...</span>
    `;
}

function hideButtonSpinner() {
    const btn = document.getElementById("btnSubmitEvtform");
    btn.disabled = false;
    btn.type = "submit";
    btn.innerHTML = '<span id="spanSave">Save</span>';
}
